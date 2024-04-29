package com.anguyen.photogram.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
