package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.EPriority
import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.request.NewTaskRequestDto
import com.herc.test.hztasklist.service.UserService
import com.herc.test.hztasklist.service.mapper.MapperToModel
import com.herc.test.hztasklist.util.DateTimeUtil
import java.util.Locale

class NewTaskRequestDtoMapper(private val userService: UserService) :
    MapperToModel<Task, NewTaskRequestDto> {

    override fun toModel(dto: NewTaskRequestDto): Task {
        val user: User? = dto.userId?.let { userId -> userService.getById(userId) }

        return Task(
            user = user,
            title = dto.title,
            description = dto.description,
            expiredTime = DateTimeUtil.toMillis(dto.expiredTime),
            isComplete = false,
            taskPriority = EPriority.fromString(dto.taskPriority.lowercase(Locale.getDefault()))
        )
    }

}