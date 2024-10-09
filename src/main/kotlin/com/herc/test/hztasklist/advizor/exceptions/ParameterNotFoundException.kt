package com.herc.test.hztasklist.advizor.exceptions

class ParameterNotFoundException(val parameter: String) :
    RuntimeException("Parameter '$parameter' not found!")
