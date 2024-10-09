package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.entity.RefreshToken
import com.herc.test.hztasklist.model.payload.dto.RefreshTokenDto
import com.herc.test.hztasklist.service.mapper.MapperToDto
import com.herc.test.hztasklist.service.mapper.MapperToModel

class RefreshTokenMapper : MapperToDto<RefreshTokenDto, RefreshToken>,
    MapperToModel<RefreshToken, RefreshTokenDto> {

    override fun toDto(entity: RefreshToken): RefreshTokenDto {
        return RefreshTokenDto(
            id = entity.id,
            token = entity.token.toString()
        )
    }

    override fun toModel(dto: RefreshTokenDto): RefreshToken {
        return RefreshToken(
            id = dto.id,
            token = dto.token
        )
    }
    }