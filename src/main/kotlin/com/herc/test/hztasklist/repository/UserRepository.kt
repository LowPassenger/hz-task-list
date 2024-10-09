package com.herc.test.hztasklist.repository

import com.herc.test.hztasklist.model.entity.RefreshToken
import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User?>

    override fun findById(id: Long): Optional<User?>

    fun findByRefreshToken(token: RefreshToken): User?

    fun existsByEmail(email: String): Boolean
}