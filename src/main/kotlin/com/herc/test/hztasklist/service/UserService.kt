package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.model.entity.RefreshToken
import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.repository.RefreshTokenRepository
import com.herc.test.hztasklist.repository.RoleRepository
import com.herc.test.hztasklist.repository.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(val userRepository: UserRepository) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    fun getById(id: Long) : User? {
        return userRepository.findById(id).orElseThrow {
            logger.error("User with id $id not found!")
            ParameterNotFoundException("User with id $id parameter not found")
        }
    }

    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun getByEmail(email: String): User? {
        return userRepository.findByEmail(email).orElseThrow {
            logger.error("User with email $email not found!")
            ParameterNotFoundException("User with id $email parameter not found")
        }
    }

    fun delete(user: Task) {
        userRepository.delete(user)
    }

    fun saveFcmToken(email: String, token: String) {
        with(getByEmail(email)) {
            fcmToken = token
            userRepository.save(this)
        }
    }

    @Transactional
    fun getRefreshToken(user: User): RefreshToken {
        return user.refreshToken ?: run {
            var newRefreshToken = generateRefreshToken()
            newRefreshToken = refreshTokenRepository.save(newRefreshToken)
            user.refreshToken = newRefreshToken
            userRepository.save(user)
            newRefreshToken
        }
    }

    private fun generateRefreshToken(): RefreshToken {
        val token = UUID.randomUUID().toString()
        return RefreshToken(token = token)
    }
}