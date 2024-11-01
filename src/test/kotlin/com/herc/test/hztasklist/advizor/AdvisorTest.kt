package com.herc.test.hztasklist.advizor

import com.fasterxml.jackson.databind.ObjectMapper
import com.herc.test.hztasklist.advizor.exceptions.BadCredentialsException
import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.advizor.exceptions.UserWithEmailExistException
import com.herc.test.hztasklist.controller.Resources
import com.herc.test.hztasklist.model.payload.dto.response.Error
import com.herc.test.hztasklist.model.payload.dto.response.ErrorResponse
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class AdvisorTest @Autowired constructor(val mockMvc: MockMvc){

    @Autowired
    lateinit var advisor: Advisor

    @Test
    fun unauthorizedReturnsWhenJsonDataWithAnnotationDataNotValidThrowsBindException_OK() {
        val invalidRequestJson = mapOf(
            "email" to "$#abblrvalg.gmail,com",
            "password" to null
        )

        mockMvc.perform(
            post(Resources.AuthApi.SIGN_IN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(invalidRequestJson))
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.error").value(containsString("Unauthorized")))
            .andExpect(jsonPath("$.message")
                .value(containsString("Full authentication is required to access this resource")))
    }

    @Test
    fun badRequestReturnsWhenEmailExistExceptionThrows_OK() {
        val testEmail = "testemail@gmail.com"
        val testException = UserWithEmailExistException(testEmail)
        val response: ResponseEntity<*> = advisor.handleEmailExistException(testException)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        val responseBody = response.body as ErrorResponse
        assertEquals("User with email $testEmail already exist!", responseBody.msg)
    }

    @Test
    fun internalServerErrorReturnsWhenParameterNotFoundExceptionThrows_OK() {
        val testIdParameter = "1"
        val testException = ParameterNotFoundException(testIdParameter)
        val response: ResponseEntity<*> = advisor.handleParameterNotFoundException(testException)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        val responseBody = response.body as ErrorResponse
        assertEquals("Parameter '$testIdParameter' not found!", responseBody.msg)
    }

    @Test
    fun forbiddenReturnsWhenBadCredentialsExceptionThrows_OK() {
        val errorObject = Error.INVALID_CREDENTIALS
        val errorMessage = "Access denied. Wrong username or password"
        val testException = BadCredentialsException()
        val response: ResponseEntity<*> = advisor.handleBadCredentialsException(testException)

        assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
        val responseBody = response.body as ErrorResponse
        assertEquals(errorObject, responseBody.error)
        assertEquals(errorMessage, responseBody.msg)
    }
}