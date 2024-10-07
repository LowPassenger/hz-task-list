package com.herc.test.hztasklist.model.payload.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Authentication Request DTO")
data class AuthenticationRequest(
    @Schema(description = "User email", example = "useremail@gmail.com")
    val email: String,

    @Schema(description = "Password", example = "Password123!")
    val password: String
)
