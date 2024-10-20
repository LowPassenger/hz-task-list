package com.herc.test.hztasklist.repository

import com.herc.test.hztasklist.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>

    override fun findById(id: Long): Optional<User>

    fun existsByEmail(email: String): Boolean

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.refreshToken WHERE u.email = :email")
    fun findByEmailWithRefreshToken(@Param("email") email: String): Optional<User>
}