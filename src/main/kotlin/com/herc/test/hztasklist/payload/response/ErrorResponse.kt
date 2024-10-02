package com.herc.test.hztasklist.payload.response

class ErrorResponse(val error: Error, val msg: String? = null)

enum class Error {
    EMAIL_EXIST,
    INVALID_REFRESH_TOKEN,
    INVALID_CREDENTIALS,
    BAD_REQUEST
}