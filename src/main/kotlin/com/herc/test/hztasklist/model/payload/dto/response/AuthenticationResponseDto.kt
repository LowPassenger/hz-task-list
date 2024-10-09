package com.herc.test.hztasklist.model.payload.dto.response

import com.herc.test.hztasklist.model.entity.Role

data class AuthenticationResponseDto(
    val id: Long?,
    val token: String?,
    val refreshToken: String?,
    val email: String,
    val role: Set<Role>
)