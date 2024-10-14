package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.response.UserResponseDto
import com.herc.test.hztasklist.service.mapper.MapperToDto
import org.springframework.stereotype.Component

@Component
class UserResponseDtoMapper : MapperToDto<UserResponseDto, User> {
    lateinit var taskMapper: TaskToResponseDtoMapper

    override fun toDto(entity: User): UserResponseDto {
        return UserResponseDto(
            id = entity.id,
            email = entity.email,
            token = entity.token,
            roles = entity.roles,
            tasks = entity.tasks.map {task -> taskMapper.toDto(task)}
        )
    }

}