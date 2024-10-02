package com.herc.test.hztasklist.repository

import com.herc.test.hztasklist.model.db.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun findByRefreshToken(token: RefreshToken): User?

    fun existsByEmail(email: String): Boolean
}