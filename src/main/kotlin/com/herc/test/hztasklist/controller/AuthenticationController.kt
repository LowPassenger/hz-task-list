package com.herc.test.hztasklist.controller

import com.herc.test.hztasklist.model.payload.dto.request.AuthenticationRequestDto
import com.herc.test.hztasklist.service.AuthenticationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
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

    @PostMapping(Resources.AuthApi.SIGN_IN)
    @Operation(summary = "Sign in procedure for the registered users")
    fun signIn(@Valid @RequestBody signinRequest: AuthenticationRequestDto): ResponseEntity<*> {
        return ResponseEntity.ok().body(authenticationService.signIn(signinRequest))
    }

    @PostMapping(Resources.AuthApi.SIGN_UP)
    @Operation(summary = "Sign up procedure new users")
    fun signUp(@Valid @RequestBody signupRequest: AuthenticationRequestDto): ResponseEntity<*> {
        return ResponseEntity.ok().body(authenticationService.signUp(signupRequest))
    }
}