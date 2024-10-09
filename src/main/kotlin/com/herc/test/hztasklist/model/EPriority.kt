package com.herc.test.hztasklist.model

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException

enum class EPriority {
    LOW,
    NORMAL,
    HIGH,
    EXTRA;

    companion object {
        fun fromString(value: String): EPriority {
            return when (value) {
                "low" -> LOW
                "normal" -> NORMAL
                "high" -> HIGH
                "extra" -> EXTRA
                else -> throw ParameterNotFoundException("No enum constant for value: $value")
            }
        }
    }
}