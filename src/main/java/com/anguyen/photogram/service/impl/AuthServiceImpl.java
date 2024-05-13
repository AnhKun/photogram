package com.anguyen.photogram.service.impl;

import com.anguyen.photogram.dto.request.LoginDto;
import com.anguyen.photogram.dto.request.LogoutRequest;
import com.anguyen.photogram.dto.request.RefreshTokenRequest;
import com.anguyen.photogram.dto.request.RegisterDto;
import com.anguyen.photogram.dto.response.ApiResponse;
import com.anguyen.photogram.dto.response.JwtAuthResponse;
import com.anguyen.photogram.entities.InvalidatedToken;
import com.anguyen.photogram.entities.RefreshToken;
import com.anguyen.photogram.entities.Role;
import com.anguyen.photogram.entities.UserEntity;
import com.anguyen.photogram.exceptions.ApiException;
import com.anguyen.photogram.exceptions.ErrorCode;
import com.anguyen.photogram.repositories.InvalidatedTokenRepository;
import com.anguyen.photogram.repositories.UserRepository;
import com.anguyen.photogram.security.SecurityConfig;
import com.anguyen.photogram.security.jwt.JwtRefreshToken;
import com.anguyen.photogram.security.jwt.JwtTokenProvider;
import com.anguyen.photogram.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRefreshToken jwtRefreshToken;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    public ApiResponse register(RegisterDto registerDto) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(registerDto.getUsername()))) {
            log.error("Username {} already exists", registerDto.getUsername());
            throw new ApiException(ErrorCode.USER_EXISTED);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);

        UserEntity newUser = UserEntity.builder()
                .email(registerDto.getEmail())
                .username(registerDto.getUsername())
                .password(SecurityConfig.passwordEncoder().encode(registerDto.getPassword()))
                .role(roles)
                .status(true)
                .build();

        userRepository.save(newUser);

        return ApiResponse.builder()
                .code(HttpStatus.CREATED)
                .result("User registered successfully")
                .build();
    }

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtRefreshToken.generateRefreshToken(loginDto.getUsername()).getRefreshTokenString();

        return JwtAuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refToken = jwtRefreshToken.verifyRefreshToken(refreshTokenRequest.getRefreshToken());

        UserEntity user = refToken.getUser();

        String accessToken = jwtTokenProvider.generateTokenFromUser(user);

        return JwtAuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .build();
    }

    @Override
    public void logout(LogoutRequest request) {
        Claims claims = jwtTokenProvider.getJwtTokenClaims(request.getToken());

        if (!(jwtTokenProvider.validateToken(request.getToken())
                && claims.getExpiration().after(new Date()))) {
            throw new ApiException(ErrorCode.UNAUTHENTICATED);
        }

        String uuid = claims.get("uuid", String.class);
        Date exp = claims.getExpiration();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(uuid)
                .expiryTime(exp)
                .build();


        invalidatedTokenRepository.save(invalidatedToken);
    }
}
