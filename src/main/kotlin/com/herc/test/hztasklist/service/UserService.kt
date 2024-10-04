package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.model.db.User
import com.herc.test.hztasklist.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {
    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun getByEmailOrThrow(email: String): User {
        return getByEmail(email) ?: throw RuntimeException("Can't load user by email $email")
    }

    fun delete(user: User) {
        userRepository.delete(user)
    }

    fun saveFcmToken(email: String, token: String) {
        with(getByEmailOrThrow(email)) {
            fcmToken = token
            userRepository.save(this)
        }
    }
}