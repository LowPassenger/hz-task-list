package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.advizor.exceptions.BadCredentialsException
import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.advizor.exceptions.UserWithEmailExistException
import com.herc.test.hztasklist.model.ERole
import com.herc.test.hztasklist.model.entity.Role
import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.request.AuthenticationRequestDto
import com.herc.test.hztasklist.model.payload.dto.response.AuthenticationResponseDto
import com.herc.test.hztasklist.repository.RoleRepository
import com.herc.test.hztasklist.repository.UserRepository
import com.herc.test.hztasklist.security.jwt.JwtUtils
import com.herc.test.hztasklist.service.mapper.impl.AuthenticationResponseDtoMapper
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService {
    private val logger = LoggerFactory.getLogger(AuthenticationService::class.java)

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var mapper: AuthenticationResponseDtoMapper

    fun getUserFromHttpRequest(request: HttpServletRequest) : User {
        val jwtFromHeader = jwtUtils.getJwtFromHeader(request)
        val emailFromJwtToken = jwtUtils.getUserEmailFromJwtToken(jwtFromHeader.toString())
        return userService.getByEmail(emailFromJwtToken.toString())
    }

    fun signIn(signinRequest: AuthenticationRequestDto) : AuthenticationResponseDto {
        val email = signinRequest.email
        val user = userRepository.findByEmail(email).orElseThrow {
            logger.error("User with email $email not found!")
            throw BadCredentialsException()
        }
        if (!encoder.matches(signinRequest.password, user.password)) {
            logger.error("User with email $email not found!")
            throw BadCredentialsException()
        }

        return mapper.toDto(user)
    }

    fun signUp(signupRequest: AuthenticationRequestDto) : AuthenticationResponseDto {
        val email = signupRequest.email
        val userExist = userService.existsByEmail(email)
        if (userExist) {
            logger.error("User with email $email already exist!")
            throw UserWithEmailExistException(email)
        }

        val userRole = roleRepository.findByName(ERole.ROLE_USER)
            .orElseThrow {
                logger.error("Role USER_ROLE was not found in the database")
                throw ParameterNotFoundException("Parameter ROLE_USER was not found in the database.")
            }
        val encodedPassword = encoder.encode(signupRequest.password)

        val userToSave = User(
            id = null,
            email = email,
            password = encodedPassword,
            token = jwtUtils.generateTokenFromEmail(email),
            refreshToken = null,
            roles = setOf(userRole),
            tasks = emptyList()
            )
        userToSave.refreshToken = userService.getRefreshToken(userToSave)

        val savedUser = userService.save(userToSave)
        return mapper.toDto(savedUser)
    }
}