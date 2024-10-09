package com.herc.test.hztasklist.model.payload.dto.response

data class UserCompleteTaskResponseDto(
    val userId: Long,
    val completedTaskList: List<TaskResponseDto>,
    val uncompletedTaskList: List<TaskResponseDto>
)
