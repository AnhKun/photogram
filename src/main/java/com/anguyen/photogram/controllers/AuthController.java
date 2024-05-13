package com.anguyen.photogram.controllers;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anguyen.photogram.dto.request.LoginDto;
import com.anguyen.photogram.dto.request.LogoutRequest;
import com.anguyen.photogram.dto.request.RefreshTokenRequest;
import com.anguyen.photogram.dto.request.RegisterDto;
import com.anguyen.photogram.dto.response.ApiResponse;
import com.anguyen.photogram.dto.response.JwtAuthResponse;
import com.anguyen.photogram.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid RegisterDto registerDto) {

        return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid LoginDto loginDto) {
        JwtAuthResponse response = authService.login(loginDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtAuthResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        JwtAuthResponse response = authService.refreshToken(refreshTokenRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
        return ResponseEntity.ok().body("Logged out!");
    }
}
