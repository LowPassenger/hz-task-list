package com.herc.test.hztasklist.model.payload.dto.response

data class AuthenticationResponseDto(
    val id: Long?,
    var token: String?,
    val refreshToken: String,
    val email: String,
    val role: List<String>
)