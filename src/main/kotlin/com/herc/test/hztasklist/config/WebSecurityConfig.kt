package com.herc.test.hztasklist.config

import com.herc.test.hztasklist.controller.Resources
import com.herc.test.hztasklist.model.ERole
import com.herc.test.hztasklist.security.jwt.AuthEntryPointJwt
import com.herc.test.hztasklist.security.jwt.AuthTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class WebSecurityConfig(
    private val unauthorizedHandler: AuthEntryPointJwt,
    private val authTokenFilter: AuthTokenFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val csrfHandler = CsrfTokenRequestAttributeHandler()
        csrfHandler.setCsrfRequestAttributeName("_csrf")

        http
            .csrf { csrf -> csrf.disable() }
            .cors {cors -> cors.disable()}
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling { handling ->
                handling.authenticationEntryPoint(unauthorizedHandler)
            }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(
                        AntPathRequestMatcher("/swagger-ui.html"),
                        AntPathRequestMatcher("/swagger-ui/**"),
                        AntPathRequestMatcher("/v3/api-docs/**"),
                        AntPathRequestMatcher("/swagger-resources/**"),
                        AntPathRequestMatcher("/webjars/**"),
                        AntPathRequestMatcher("/v3/api-docs.yaml"),
                        AntPathRequestMatcher("/error")
                    ).permitAll()
                    .requestMatchers(AntPathRequestMatcher("${Resources.AuthApi.ROOT}/**"))
                    .permitAll()
                    .requestMatchers(AntPathRequestMatcher("${Resources.AdminApi.ROOT}/**"))
                    .hasRole(ERole.ROLE_ADMIN.withoutPrefix())
                    .anyRequest().authenticated()
            }

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): (HttpSecurity) -> Unit = { http ->
        http.securityMatcher(AntPathRequestMatcher("/resources/**"))
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()
            }
    }
}