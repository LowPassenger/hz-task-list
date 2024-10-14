package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.EPriority
import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.request.NewTaskRequestDto
import com.herc.test.hztasklist.service.UserService
import com.herc.test.hztasklist.service.mapper.MapperToModel
import com.herc.test.hztasklist.util.DateTimeUtil
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.Locale

@Component
class NewTaskRequestDtoMapper(private val userService: UserService) :
    MapperToModel<Task, NewTaskRequestDto> {

    override fun toModel(dto: NewTaskRequestDto): Task {
        return Task(
            title = dto.title,
            description = dto.description,
            timeStamp = DateTimeUtil.toMillis(LocalDateTime.now()),
            expiredTime = DateTimeUtil.toMillis(dto.expiredTime),
            isComplete = false,
            taskPriority = EPriority.fromString(dto.taskPriority.lowercase(Locale.getDefault()))
        )
    }

}