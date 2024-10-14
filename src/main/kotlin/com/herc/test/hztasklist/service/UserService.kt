package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.model.entity.RefreshToken
import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.response.UserResponseDto
import com.herc.test.hztasklist.repository.RefreshTokenRepository
import com.herc.test.hztasklist.repository.UserRepository
import com.herc.test.hztasklist.service.mapper.impl.UserResponseDtoMapper
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(val userRepository: UserRepository) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Autowired
    lateinit var mapper: UserResponseDtoMapper

    fun getById(id: Long) : User {
        return userRepository.findById(id).orElseThrow {
            logger.error("User with id $id not found!")
            ParameterNotFoundException("User with id $id parameter not found")
        }
    }

    fun getAll() : List<UserResponseDto> {
        val usersList = userRepository.findAll()
        return usersList.map {user -> mapper.toDto(user)}
    }

    fun existsByEmail(email: String) : Boolean {
        return userRepository.existsByEmail(email)
    }

    fun getByEmail(email: String) : User {
        return userRepository.findByEmail(email).orElseThrow {
            logger.error("User with email $email not found!")
            ParameterNotFoundException("User with id $email parameter not found")
        }
    }

    fun save(user: User) : User {
       return userRepository.save(user)
    }

    fun delete(user: User) {
        userRepository.delete(user)
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