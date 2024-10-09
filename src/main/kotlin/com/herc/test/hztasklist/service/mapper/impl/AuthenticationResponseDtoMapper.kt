package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.response.AuthenticationResponseDto
import com.herc.test.hztasklist.service.mapper.MapperToDto

class AuthenticationResponseDtoMapper : MapperToDto<AuthenticationResponseDto, User> {
    override fun toDto(entity: User): AuthenticationResponseDto {
        return AuthenticationResponseDto(
            id = entity.id,
            token = entity.token,
            refreshToken = entity.refreshToken.toString(),
            email = entity.email,
            role = entity.roles
        )
    }
}