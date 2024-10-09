package com.herc.test.hztasklist.model.payload.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

@Schema(description = "Authentication Request DTO")
data class AuthenticationRequestDto(
    @NotNull(message = "Email is required")
    @Length(max = 50, message = "Email must include less or equals than 50 characters")
    @Schema(description = "User email", example = "useremail@gmail.com")
    val email: String,

    @NotNull(message = "Password is required")
    @Length(min = 8, max = 24, message = "Password must include more than 7 and "
            + "less or equals than 24 characters")
    @Schema(description = "Password", example = "Password123!")
    val password: String
)
