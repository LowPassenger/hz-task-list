package com.herc.test.hztasklist.controller

import com.herc.test.hztasklist.model.payload.dto.response.RefreshTokenResponseDto
import com.herc.test.hztasklist.model.payload.dto.request.AuthenticationRequestDto
import com.herc.test.hztasklist.service.AuthenticationService
import com.herc.test.hztasklist.service.RefreshTokenService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Resources.AuthApi.ROOT)
@Tag(name = "Authentication Controller", description = "Task API authentication")
class AuthenticationController {
    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var refreshTokenService: RefreshTokenService

    @PostMapping(Resources.AuthApi.SIGN_IN)
    @Operation(summary = "Sign in procedure for the registered users")
    fun signIn(@Valid @RequestBody signinRequest: AuthenticationRequestDto): ResponseEntity<*> {
        return ResponseEntity.ok().body(authenticationService.signIn(signinRequest))
    }

    @PostMapping(Resources.AuthApi.SIGN_UP)
    @Operation(summary = "Sign up procedure new users")
    fun signUp(@Valid @RequestBody signupRequest: AuthenticationRequestDto): ResponseEntity<*> {
        return if (authenticationService.signUp(signupRequest))
            ResponseEntity.ok().body("User successfully registered")
        else
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User was not register!")
    }

    @PostMapping(Resources.AuthApi.REFRESH)
    @Operation(summary = "Refresh JWT token procedure")
    fun refreshTokenCheck(@Valid @RequestBody refreshRequest: RefreshTokenResponseDto):
            ResponseEntity<*> {
        val refreshResponse = refreshTokenService.refreshMe(refreshRequest)
        return ResponseEntity.ok().body(refreshResponse)

    }
}