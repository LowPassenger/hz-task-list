package com.herc.test.hztasklist.model.payload.dto.response

import com.herc.test.hztasklist.model.EPriority

data class TaskResponseDto(
    val id: Long?,
    val title: String?,
    val timeStamp: Long,
    val expiredTime: Long,
    val isComplete: Boolean,
    val taskPriority: EPriority,
    val user: UserResponseDto?
)
