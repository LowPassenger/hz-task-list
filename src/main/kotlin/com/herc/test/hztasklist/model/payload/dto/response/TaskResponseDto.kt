package com.herc.test.hztasklist.model.payload.dto.response

data class TaskResponseDto(
    val id: Long,
    val title: String,
    val description: String?,
    val timeStamp: String,
    val expiredTime: String,
    val isComplete: Boolean,
    val taskPriority: String,
    val userId: Long
)
