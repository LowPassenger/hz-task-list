package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.EPriority
import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.request.AdminChangeTaskRequestDto
import com.herc.test.hztasklist.service.UserService
import com.herc.test.hztasklist.service.mapper.MapperToModel
import com.herc.test.hztasklist.util.DateTimeUtil
import org.springframework.stereotype.Component
import java.util.*

@Component
class AdminChangeTaskRequestMapper (private val userService: UserService) :
    MapperToModel<Task, AdminChangeTaskRequestDto> {
    override fun toModel(dto: AdminChangeTaskRequestDto): Task {
        val user: User? = dto.userId?.let { userId -> userService.getById(userId) }

        return Task(
            title = dto.title,
            description = dto.description,
            expiredTime = DateTimeUtil.toMillis(dto.expiredTime),
            isComplete = dto.isComplete,
            taskPriority = EPriority.fromString(dto.taskPriority.lowercase(Locale.getDefault())),
            user = user
        )
    }

}