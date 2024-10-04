package com.herc.test.hztasklist.controller.utils

import org.springframework.security.core.context.SecurityContextHolder

object ControllerUtils {
    fun getUserEmailFromCurrentContext() =
        SecurityContextHolder.getContext().authentication.name
}