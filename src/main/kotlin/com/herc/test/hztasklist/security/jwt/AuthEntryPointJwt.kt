package com.herc.test.hztasklist.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.herc.test.hztasklist.controller.Resources
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class AuthEntryPointJwt: AuthenticationEntryPoint {
    private val logger = LoggerFactory.getLogger(AuthEntryPointJwt::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.error("Unauthorized error: {}", authException.message)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        val body: MutableMap<String, Any?> = HashMap()
        body["status"] = HttpServletResponse.SC_UNAUTHORIZED
        body["error"] = "Unauthorized"
        if (request.servletPath.contains(Resources.AdminApi.ROOT)) {
            body["login_path"] = Resources.AdminApi.ROOT
        }
        body["message"] = authException.message
        body["timestamp"] = System.currentTimeMillis()
        body["path"] = request.servletPath
        val mapper = ObjectMapper()
        mapper.writeValue(response.outputStream, body)
    }
}