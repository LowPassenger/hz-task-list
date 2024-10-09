package com.herc.test.hztasklist.advizor;

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.advizor.exceptions.UserWithEmailExistException
import com.herc.test.hztasklist.model.payload.dto.response.Error
import com.herc.test.hztasklist.model.payload.dto.response.ErrorResponse
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class Advisor {
    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleDataNotValid(e: BindException): ResponseEntity<*> {

        val errors = e.bindingResult.fieldErrors.joinToString {
            "field ${it.field} has error ${it.defaultMessage}"
        }

        return ResponseEntity.badRequest().body(ErrorResponse(Error.BAD_REQUEST, errors))
}
    @ExceptionHandler(UserWithEmailExistException::class)
    @ResponseBody
    fun handleEmailExistException(e: UserWithEmailExistException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(Error.EMAIL_EXIST, e.message))
    }

    @ExceptionHandler(ParameterNotFoundException::class)
    @ResponseBody
    fun handleParameterNotFoundException(e: ParameterNotFoundException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(Error.INTERNAL_SERVER_ERROR, e.message))
    }
}
