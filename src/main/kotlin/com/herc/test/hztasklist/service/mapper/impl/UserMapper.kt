package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.request.UserRequestDto
import com.herc.test.hztasklist.model.payload.dto.response.UserResponseDto
import com.herc.test.hztasklist.service.mapper.MapperToDto
import com.herc.test.hztasklist.service.mapper.MapperToModel

class UserMapper(
    private val roleMapper: RoleMapper,
    private val taskMapper: TaskMapper
) : MapperToDto<UserResponseDto, User>, MapperToModel<User, UserRequestDto> {

    override fun toDto(entity: User): UserResponseDto {
        return UserResponseDto(
            id = entity.id,
            email = entity.email,
            fcmToken = entity.fcmToken,
            roles = entity.roles.map { roleMapper.toDto(it) }.toSet(),
            tasks = entity.tasks.map { taskMapper.toDto(it) }
        )
    }

    override fun toModel(dto: UserRequestDto): User {
        val user = User(
            email = dto.email,
            password = dto.password,
            fcmToken = dto.fcmToken
        )
        return user
    }
}