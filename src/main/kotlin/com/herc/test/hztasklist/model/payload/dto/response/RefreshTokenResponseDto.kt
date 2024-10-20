package com.herc.test.hztasklist.model.payload.dto.response

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RefreshTokenResponseDto(
    @NotNull
    @NotBlank(message = "JWT Token cannot be blank")
    val jwtToken: String,

    @NotNull
    @NotBlank(message = "Refresh Token cannot be blank")
    val refreshToken: String
)