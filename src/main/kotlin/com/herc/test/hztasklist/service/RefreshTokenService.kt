package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.model.entity.RefreshToken
import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.response.RefreshTokenResponseDto
import com.herc.test.hztasklist.repository.RefreshTokenRepository
import com.herc.test.hztasklist.security.jwt.JwtUtils
import io.jsonwebtoken.ExpiredJwtException
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
        val email = try {
            jwtUtils.getUserEmailFromJwtToken(refreshRequest.jwtToken)
        } catch (exception: ExpiredJwtException) {
            logger.warn("JWT token expired for refresh request: ${exception.message}")
            exception.claims.subject
        } catch (exception: Exception) {
            logger.error("An error occurred during token parsing: ${exception.message}")
            throw ParameterNotFoundException("Valid JWT token")
        }
        if (email == null || !userService.existsByEmail(email)) {
            logger.error("An error occurred during refresh token operation: email in "
                    + "JWT token not found or null or user with this email not found")
            throw ParameterNotFoundException("email in JWT token or user with this email")
        }

        val user = userService.getUserByEmailWithRefreshToken(email)
        var userRefreshToken = user.refreshToken

        if (userRefreshToken == null) {
            userRefreshToken = getRefreshToken(user)
            return RefreshTokenResponseDto(jwtUtils.generateTokenFromEmail(email),
                userRefreshToken.token!!)
        }

        if (refreshRequest.refreshToken != userRefreshToken.token) {
            logger.error("An error occurred during refresh token operation: refreshToken for "
                    + "user with email $email is not equal to refreshToken from DB")
            throw ParameterNotFoundException("equal refresh token in db")
        }

        val refreshTokenTimeStamp = userRefreshToken.timeStamp

        if (Date().after(Date(refreshTokenTimeStamp + refreshExpirationMs))) {
            logger.error("An error occurred during refresh token operation: refreshToken for "
                    + "user with email $email is expired")
            throw ParameterNotFoundException("expired refresh token")
        }

        var refreshTokenUseCounter = userRefreshToken.counter

        if (refreshTokenUseCounter <= refreshTokenMaxCounter) {
            userRefreshToken.counter = ++refreshTokenUseCounter
            refreshTokenRepository.save(userRefreshToken)
            return RefreshTokenResponseDto(jwtUtils.generateTokenFromEmail(email),
                userRefreshToken.token!!)
        }

        val generatedRefreshToken = generateRefreshToken()
        userRefreshToken.token = generatedRefreshToken.token
        userRefreshToken.counter = generatedRefreshToken.counter
        userRefreshToken.timeStamp = generatedRefreshToken.timeStamp
        val savedRefreshToken = refreshTokenRepository.save(userRefreshToken)

        return RefreshTokenResponseDto(jwtUtils.generateTokenFromEmail(email), savedRefreshToken.token!!)
    }

    fun getRefreshToken(user: User): RefreshToken {
        var refreshToken = user.refreshToken
        if (user.refreshToken == null) {
            var newRefreshToken = generateRefreshToken()
            newRefreshToken = refreshTokenRepository.save(newRefreshToken)
            user.refreshToken = newRefreshToken
            userService.save(user)
            refreshToken = newRefreshToken
        }
        return refreshToken!!
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