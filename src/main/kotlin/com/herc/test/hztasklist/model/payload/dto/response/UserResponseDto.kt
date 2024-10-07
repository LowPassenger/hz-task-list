package com.herc.test.hztasklist.model.payload.dto.response

import com.herc.test.hztasklist.model.payload.dto.RoleDto

data class UserResponseDto(
    val id: Long?,
    val email: String,
    val fcmToken: String?,
    val roles: Set<RoleDto>,
    val tasks: List<TaskResponseDto>
)
