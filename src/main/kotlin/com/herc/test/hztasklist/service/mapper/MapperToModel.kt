package com.herc.test.hztasklist.service.mapper

interface MapperToModel<E, D> {
    fun toModel(dto: D): E
}