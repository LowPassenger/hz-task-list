package com.herc.test.hztasklist.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var userDetailsService: UserDetailsServiceImpl


    @Autowired
    private lateinit var unauthorizedHandler: AuthEntryPointJwt

    @Autowired
    private lateinit var environment: Environment

    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter {
        return AuthTokenFilter()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }

    @Bean
    @Throws(java.lang.Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity) {

        val configure = http.cors().disable().csrf()
            .disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().antMatchers("${Resources.AuthApi.ROOT}/**").permitAll()
            .antMatchers("${Resources.TestApi.ROOT}/**").permitAll()
            .antMatchers("${Resources.PasswordApi.ROOT}/**").permitAll()
            .antMatchers("${Resources.AdminApi.ROOT}${Resources.AdminApi.LOGIN}").permitAll()
            .antMatchers("/favicon.ico").permitAll()
            .antMatchers("${Resources.AdminApi.ROOT}/**").hasRole(ERole.ROLE_ADMIN.withoutPrefix())

        if (environment.activeProfiles.contains(DEV_PROFILE)) {
            configure
                .antMatchers("${Resources.Static.OpenApi.ROOT_PATH}/*").permitAll()
                .antMatchers("${Resources.Static.OpenApi.DOCS_PATH}/*").permitAll()
                .antMatchers(Resources.Static.OpenApi.DOCS_PATH).permitAll()
                .antMatchers("/${Resources.Static.Photo.FOLDER}/**").permitAll()
        }

        configure.anyRequest().authenticated()

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)

    }
}