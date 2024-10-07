package com.herc.test.hztasklist.model.payload.dto.response

import com.herc.test.hztasklist.model.ERole

data class AuthenticationResponse(
    val id: Long,
    val token: String,
    val refreshToken: String,
    val email: String,
    val role: ERole
)