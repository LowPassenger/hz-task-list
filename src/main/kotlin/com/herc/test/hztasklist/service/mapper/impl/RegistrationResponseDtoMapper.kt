package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.response.RegistrationResponseDto
import com.herc.test.hztasklist.service.mapper.MapperToDto
import org.springframework.stereotype.Component

@Component
class RegistrationResponseDtoMapper : MapperToDto<RegistrationResponseDto, User> {
    override fun toDto(entity: User): RegistrationResponseDto {
        return RegistrationResponseDto(
            id = entity.id,
            email = entity.email,
            roles = entity.roles
        )
    }
}