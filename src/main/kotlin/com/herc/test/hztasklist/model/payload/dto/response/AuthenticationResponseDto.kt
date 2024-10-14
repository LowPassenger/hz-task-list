package com.herc.test.hztasklist.model.payload.dto.response

data class AuthenticationResponseDto(
    val id: Long?,
    val token: String?,
    val email: String,
    val role: List<String>
)