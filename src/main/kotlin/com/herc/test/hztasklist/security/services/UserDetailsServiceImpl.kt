package com.herc.test.hztasklist.security.services

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl : UserDetailsService {
    private val logger = LoggerFactory.getLogger(UserDetailsServiceImpl::class.java)

    @Autowired
    lateinit var userRepository: UserRepository

    @Transactional
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email).orElseThrow {
            logger.error("User with email $email not found!")
            ParameterNotFoundException("User with id $email parameter not found")
        }
        return UserDetailsImpl(user)
    }
}