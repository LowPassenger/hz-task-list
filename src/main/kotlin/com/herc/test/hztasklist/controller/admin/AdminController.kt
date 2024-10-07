package com.herc.test.hztasklist.controller.admin

import com.herc.test.hztasklist.controller.Resources
import com.herc.test.hztasklist.repository.UserRepository
import com.herc.test.hztasklist.security.jwt.JwtUtils
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import jakarta.servlet.http.HttpServletRequest

@Controller
@RequestMapping(Resources.AdminApi.ROOT)
class AdminController {
    private val logger = LoggerFactory.getLogger(AdminController::class.java)

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userRepository: UserRepository

    @PostMapping(Resources.AdminApi.LOGIN)
    fun login(
        @Valid @ModelAttribute adminViewParams: AdminViewParams,
        request: HttpServletRequest
    ): String {
        val authentication: Authentication = authenticationManager
            .authenticate(UsernamePasswordAuthenticationToken(adminViewParams.email, adminViewParams.password))

        SecurityContextHolder.getContext().authentication = authentication
        jwtUtils.generateAndIncludeJwtForSession(request, adminViewParams.email)

        logger.info("Admin ${adminViewParams.email} log to AdminAPI")
        return "redirect:${Resources.AdminApi.ROOT}${Resources.AdminApi.PHOTOS_TO_APPROVE}"
    }
}