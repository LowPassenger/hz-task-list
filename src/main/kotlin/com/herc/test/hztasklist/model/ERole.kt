package com.herc.test.hztasklist.model

enum class ERole {
    ROLE_USER,
    ROLE_ADMIN;

    fun withoutPrefix(): String {
        return this.name.substring("ROLE_".length)
    }
}