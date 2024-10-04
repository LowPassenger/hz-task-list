package com.herc.test.hztasklist.controller.admin

import com.herc.test.hztasklist.controller.Resources
import com.herc.test.hztasklist.model.ERole
import com.herc.test.hztasklist.repository.UserRepository
import com.herc.test.hztasklist.security.jwt.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

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

    @GetMapping(Resources.AdminApi.LOGIN)
    fun loginPage(): ModelAndView {
        val mav = SecurityContextHolder.getContext().authentication?.let {
            if (it.isAuthenticated && it.authorities.any { it.authority == ERole.ROLE_ADMIN.name }) {
                ModelAndView("redirect:${Resources.AdminApi.ROOT}")
            } else {
                null
            }
        } ?: run {
            val mav = ModelAndView(Resources.Static.Admin.LOGIN)
            mav.addObject("admin", AdminViewParams("", ""))
            mav.addObject("submitEndpoint", "${Resources.AdminApi.ROOT}${Resources.AdminApi.LOGIN}");
        }
        return mav
    }
}