package com.anguyen.photogram.dto.request;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotEmpty(message = "Please provide the refresh token")
    private String refreshToken;
}
