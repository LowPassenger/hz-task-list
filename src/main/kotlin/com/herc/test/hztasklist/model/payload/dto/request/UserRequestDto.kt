package com.herc.test.hztasklist.model.payload.dto.request

data class UserRequestDto(
    val email: String,
    val password: String,
    val fcmToken: String? = null,
    val roleIds: Set<Long>,
    val taskIds: List<Long>
)
