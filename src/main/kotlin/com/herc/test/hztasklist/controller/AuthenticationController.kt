package com.herc.test.hztasklist.controller

import com.herc.test.hztasklist.model.payload.dto.request.AuthenticationRequestDto
import com.herc.test.hztasklist.security.jwt.JwtUtils
import com.herc.test.hztasklist.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Resources.AuthApi.ROOT)
class AuthenticationController {
    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @PostMapping(Resources.AuthApi.SIGN_IN)
    fun signIn(
        request: HttpServletRequest,
        @Valid @RequestBody signinRequest: AuthenticationRequestDto
    ): ResponseEntity<*> {
        return try {
            val authentication: Authentication = authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken(signinRequest.email, signinRequest.password))

            SecurityContextHolder.getContext().authentication = authentication

            val user = userService.getByEmail(signinRequest.email)

            val jwt = jwtUtils.generateTokenFromEmail(signinRequest.email)
            val refreshToken = userService.getRefreshToken(user)

            ResponseEntity.ok(SuccessfulAuthResponse(jwt, refreshToken.token))
        } catch (exeption: AuthenticationException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(Error.INVALID_CREDENTIALS))
        }
    }
}