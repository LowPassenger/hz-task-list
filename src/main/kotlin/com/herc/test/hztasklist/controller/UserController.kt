package com.herc.test.hztasklist.controller

import com.herc.test.hztasklist.controller.ControllerUtils
import com.herc.test.hztasklist.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Resources.UserApi.ROOT)
class UserController {
    @Autowired
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @PostMapping(Resources.UserApi.FCM_TOKEN)
    fun saveFcmToken(@RequestParam token: String): ResponseEntity<*> {
        return ResponseEntity.ok(userService.saveFcmToken(ControllerUtils.getUserEmailFromCurrentContext(), token))
    }
}