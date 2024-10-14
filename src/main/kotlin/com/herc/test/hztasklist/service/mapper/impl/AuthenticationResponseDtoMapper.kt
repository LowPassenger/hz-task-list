package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.response.AuthenticationResponseDto
import com.herc.test.hztasklist.service.UserService
import com.herc.test.hztasklist.service.mapper.MapperToDto
import org.springframework.stereotype.Component

@Component
class AuthenticationResponseDtoMapper : MapperToDto<AuthenticationResponseDto, User> {
    override fun toDto(entity: User): AuthenticationResponseDto {
        val roles = entity.roles.map { it.name.toString() }

        return AuthenticationResponseDto(
            id = entity.id,
            token = entity.token,
            email = entity.email,
            role = roles
        )
    }
}