package com.herc.test.hztasklist.security.jwt

import com.herc.test.hztasklist.controller.Resources
import com.herc.test.hztasklist.security.services.UserDetailsServiceImpl
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.filter.OncePerRequestFilter

class AuthTokenFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtUtils: JwtUtils

    private val authLogger: Logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)

    @Autowired
    private lateinit var userDetailsService: UserDetailsServiceImpl

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        try {
            val jwt = parseJwt(request)
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                val email: String = jwtUtils.getUserEmailFromJwtToken(jwt)
                    ?: throw RuntimeException("Can't retrieve name from auth token")
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(email)
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            authLogger.error("Cannot set user authentication: {}", e.message)
        }

        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        return if (request.servletPath.startsWith(Resources.AdminApi.ROOT)) {
            jwtUtils.getJwtFromSession(request)
        } else {
            jwtUtils.getJwtFromHeader(request)
        }
    }
}