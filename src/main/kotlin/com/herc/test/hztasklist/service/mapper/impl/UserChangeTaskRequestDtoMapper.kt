package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.EPriority
import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.model.payload.dto.request.UserChangeTaskRequestDto
import com.herc.test.hztasklist.service.mapper.MapperToModel
import com.herc.test.hztasklist.util.DateTimeUtil
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserChangeTaskRequestDtoMapper :
    MapperToModel<Task, UserChangeTaskRequestDto> {
    override fun toModel(dto: UserChangeTaskRequestDto): Task {        
        return Task(
            id = dto.taskId,
            title = dto.title,
            description = dto.description,
            expiredTime = DateTimeUtil.toMillis(dto.expiredTime),
            isComplete = dto.isComplete,
            taskPriority = EPriority.fromString(dto.taskPriority.lowercase(Locale.getDefault())),
        )
    }
}