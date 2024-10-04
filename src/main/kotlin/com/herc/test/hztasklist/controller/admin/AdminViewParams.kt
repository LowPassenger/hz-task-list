package com.herc.test.hztasklist.controller.admin

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

class AdminViewParams (
    @field:NotBlank
    @field:Size(max = 50)
    @field:Email
    var email: String,

    @field:NotBlank
    @field:Size(min = 8, max = 30)
    var password: String
)
