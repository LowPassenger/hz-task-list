package com.herc.test.hztasklist.service.mapper

interface MapperToDto<D, E> {
    fun toDto(entity: E): D
}