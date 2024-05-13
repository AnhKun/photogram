package com.anguyen.photogram.service;

import com.anguyen.photogram.dto.request.LoginDto;
import com.anguyen.photogram.dto.request.LogoutRequest;
import com.anguyen.photogram.dto.request.RefreshTokenRequest;
import com.anguyen.photogram.dto.request.RegisterDto;
import com.anguyen.photogram.dto.response.ApiResponse;
import com.anguyen.photogram.dto.response.JwtAuthResponse;

public interface AuthService {
    ApiResponse<Object> register(RegisterDto registerDto);

    JwtAuthResponse login(LoginDto loginDto);

    JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void logout(LogoutRequest request);
}
