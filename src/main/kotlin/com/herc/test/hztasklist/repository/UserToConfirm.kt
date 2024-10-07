package com.herc.test.hztasklist.repository

import org.springframework.data.jpa.repository.JpaRepository

interface UserToConfirm : JpaRepository<UserToConfirm,Long> {
    fun findByEmail(email: String): UserToConfirm?

    fun existsByEmail(email: String): Boolean
}