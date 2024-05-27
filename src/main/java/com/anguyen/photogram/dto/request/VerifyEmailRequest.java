package com.anguyen.photogram.dto.request;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyEmailRequest {
    @NotEmpty
    private String email;
}
