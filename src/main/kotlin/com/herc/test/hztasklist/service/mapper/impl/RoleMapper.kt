package com.herc.test.hztasklist.service.mapper.impl

import com.herc.test.hztasklist.model.ERole
import com.herc.test.hztasklist.model.entity.Role
import com.herc.test.hztasklist.model.payload.dto.RoleDto
import com.herc.test.hztasklist.service.mapper.MapperToDto
import com.herc.test.hztasklist.service.mapper.MapperToModel

class RoleMapper : MapperToDto<RoleDto, Role>, MapperToModel<Role, RoleDto> {

    override fun toDto(entity: Role): RoleDto {
        return RoleDto(
            id = entity.id,
            name = entity.name.name
        )
    }

    override fun toModel(dto: RoleDto): Role {
        return Role(
            id = dto.id,
            name = ERole.valueOf(dto.name)
        )
    }
}