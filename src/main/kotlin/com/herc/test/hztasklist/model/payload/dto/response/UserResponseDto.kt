package com.herc.test.hztasklist.model.payload.dto.response

import com.herc.test.hztasklist.model.entity.Role

data class UserResponseDto(
    val id: Long?,
    val email: String,
    val roles: Set<Role>,
    val tasks: List<TaskResponseDto>
)
