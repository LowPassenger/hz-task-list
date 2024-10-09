package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.model.entity.RefreshToken
import com.herc.test.hztasklist.repository.RefreshTokenRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RefreshTokenService(val tokenRepository: RefreshTokenRepository) {
    private val logger = LoggerFactory.getLogger(RefreshTokenService::class.java)

    fun getRefreshToken(token: String) : RefreshToken? {
        return tokenRepository.findByToken(token).orElseThrow {
            logger.error("Refresh token for the token $token not found!")
            ParameterNotFoundException("Refresh token for the token $token parameter not found")
        }
    }
}