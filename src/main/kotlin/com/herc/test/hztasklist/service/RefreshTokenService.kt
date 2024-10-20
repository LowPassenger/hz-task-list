package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.model.entity.RefreshToken
import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.response.RefreshTokenResponseDto
import com.herc.test.hztasklist.repository.RefreshTokenRepository
import com.herc.test.hztasklist.security.jwt.JwtUtils
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class RefreshTokenService(val refreshTokenRepository: RefreshTokenRepository) {
    private val logger = LoggerFactory.getLogger(RefreshTokenService::class.java)

    @Value("\${com.herc.test.hztasklist.jwt.refreshExpirationMs}")
    private var refreshExpirationMs: Long = 0

    @Value("\${com.herc.test.hztasklist.jwt.refreshTokenMaxCounter}")
    private var refreshTokenMaxCounter: Int = 0

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var jwtUtils: JwtUtils

    fun refreshMe(refreshRequest: RefreshTokenResponseDto) : RefreshTokenResponseDto {
        val email = jwtUtils.getUserEmailFromJwtToken(refreshRequest.jwtToken)
        if (email == null || userService.existsByEmail(email)) {
            logger.error("An error occurred during refresh token operation: email in "
                    + "JWT token not found or null or user with this email not found")
            throw ParameterNotFoundException("email in JWT token or user with this email")
        }

        val user = userService.getUserByEmailWithRefreshToken(email)
        val userRefreshToken = user.refreshToken

        if (userRefreshToken == null || refreshRequest.refreshToken != userRefreshToken.token) {
            logger.error("An error occurred during refresh token operation: refreshToken for "
                    + "user with email $email is null or not equal with refreshToken from DB")
            throw ParameterNotFoundException("correct refresh token")
        }

        val refreshTokenTimeStamp = userRefreshToken.timeStamp
        var refreshTokenUseCounter = userRefreshToken.counter

        if (Date().after(Date(refreshTokenTimeStamp + refreshExpirationMs))) {
            logger.error("An error occurred during refresh token operation: refreshToken for "
                    + "user with email $email is expired")
            throw ParameterNotFoundException("expired refresh token")
        }

        if (refreshTokenUseCounter >= refreshTokenMaxCounter) {
            userRefreshToken.counter = ++refreshTokenUseCounter
            refreshTokenRepository.save(userRefreshToken)
            return RefreshTokenResponseDto(jwtUtils.generateTokenFromEmail(email), userRefreshToken.token!!)
        }

        return RefreshTokenResponseDto(jwtUtils.generateTokenFromEmail(email), getRefreshToken(user).token!!)
    }

    fun getRefreshToken(user: User): RefreshToken {
        return user.refreshToken ?: run {
            var newRefreshToken = generateRefreshToken()
            newRefreshToken = refreshTokenRepository.save(newRefreshToken)
            user.refreshToken = newRefreshToken
            userService.save(user)
            newRefreshToken
        }
    }

    private fun generateRefreshToken(): RefreshToken {
        val token = UUID.randomUUID().toString()
        return RefreshToken(
            token = token,
            counter = 0,
            timeStamp = Date().time
        )
    }
}