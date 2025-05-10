package com.nhnacademy.springsecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username은 필수입니다.")
        @Size(min = 3, max = 20, message = "Username은 3~20자여야 합니다.")
        String username,

        @NotBlank(message = "Password는 필수입니다.")
        @Size(min = 6, message = "Password는 최소 6자여야 합니다.")
        String password
) { }
