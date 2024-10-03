package com.herc.test.hztasklist.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtils {
    private val logger = LoggerFactory.getLogger(JwtUtils::class.java)

    @Value("{com.herc.test.hztasklist.jwt.jwtSecret}")
    private lateinit var jwtSecret: String

    @Value("{com.herc.test.hztasklist.jwt.jwtExpirationMs}")
    private var jwtExpirationMs = 0

    @Value("{com.herc.test.hztasklist.jwt.jwtHeaderName}")
    private lateinit var jwtHeaderName: String

    @Value("{com.herc.test.hztasklist.jwt.jwtTokenPrefix}")
    private lateinit var jwtTokenPrefix: String

    fun getJwtFromHeader(request: HttpServletRequest): String? =
        request.getHeader(jwtHeaderName)?.takeIf { it.startsWith(jwtTokenPrefix) }?.let {
            it.replace(jwtTokenPrefix, "")
        }

    fun getUserEmailFromJwtToken(token: String): String? {
        return Jwts.parserBuilder()
            .setSigningKey(jwtSecret.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecret.toByteArray())
                .build()
                .parseClaimsJws(authToken)
            return true
        } catch (e: SecurityException) {
            logger.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }

    fun generateTokenFromEmail(email: String?): String {
        val signingKey: Key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(signingKey, SignatureAlgorithm.HS512)
            .compact()
    }
}