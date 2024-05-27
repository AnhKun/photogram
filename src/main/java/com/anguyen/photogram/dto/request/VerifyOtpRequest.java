package com.anguyen.photogram.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequest {
    @NotEmpty
    private String email;

    @NotNull
    private Integer otp;
}
