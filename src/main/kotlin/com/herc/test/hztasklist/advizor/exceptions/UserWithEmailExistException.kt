package com.herc.test.hztasklist.advizor.exceptions

class UserWithEmailExistException(val email:String) :
    RuntimeException("User with email $email already exist!")