package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.model.payload.dto.request.TaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.response.TaskResponseDto
import com.herc.test.hztasklist.service.mapper.MapperToDto
import com.herc.test.hztasklist.service.mapper.MapperToModel

class TaskMapper(
    private val userMapper: UserMapper
) : MapperToDto<TaskResponseDto, Task>, MapperToModel<Task, TaskRequestDto> {

    override fun toDto(entity: Task): TaskResponseDto {
        return TaskResponseDto(
            id = entity.id,
            title = entity.title,
            timeStamp = entity.timeStamp,
            expiredTime = entity.expiredTime,
            isComplete = entity.isComplete,
            taskPriority = entity.taskPriority,
            user = entity.user?.let { userMapper.toDto(it) }
        )
    }

    override fun toModel(dto: TaskRequestDto): Task {
        return Task(
            title = dto.title,
            timeStamp = dto.timeStamp,
            expiredTime = dto.expiredTime,
            isComplete = dto.isComplete,
            taskPriority = dto.taskPriority
        )
    }
}