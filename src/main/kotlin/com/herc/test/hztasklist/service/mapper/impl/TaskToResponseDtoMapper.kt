package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.model.payload.dto.response.TaskResponseDto
import com.herc.test.hztasklist.service.mapper.MapperToDto
import com.herc.test.hztasklist.util.DateTimeUtil
import org.springframework.stereotype.Component

@Component
class TaskToResponseDtoMapper : MapperToDto<TaskResponseDto, Task> {
    override fun toDto(entity: Task): TaskResponseDto {
        return TaskResponseDto(
            id = entity.id!!,
            title = entity.title.toString(),
            description = entity.description ?: "",
            timeStamp = DateTimeUtil.fromMillis(entity.timeStamp).toString(),
            expiredTime = DateTimeUtil.fromMillis(entity.expiredTime).toString(),
            isComplete = entity.isComplete,
            taskPriority = entity.taskPriority.name,
            userId = entity.user!!.id!!
        )
    }
}