package com.anguyen.photogram.security.jwt;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.anguyen.photogram.entities.RefreshToken;
import com.anguyen.photogram.entities.UserEntity;
import com.anguyen.photogram.exceptions.ApiException;
import com.anguyen.photogram.exceptions.ErrorCode;
import com.anguyen.photogram.repositories.RefreshTokenRepository;
import com.anguyen.photogram.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtRefreshToken {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.refresh-token-expiration-mil}")
    private Long refreshTokenExpirationMs;

    // generation refresh token from username
    public RefreshToken generateRefreshToken(String username) {
        UserEntity existingUser = userRepository
                .findByUsernameAndStatusTrue(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: " + username);
                    throw new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "User not found");
                });

        RefreshToken refreshToken = existingUser.getRefreshToken();

        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshTokenString(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenExpirationMs))
                    .user(existingUser)
                    .build();

            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    // verify the refresh token
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken existingRefreshToken = refreshTokenRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow(() -> {
                    log.error("Refresh token not found");
                    throw new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "Refresh token not found");
                });

        if (existingRefreshToken.getExpirationTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(existingRefreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return existingRefreshToken;
    }
}
