package com.herc.test.hztasklist.model

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import java.util.Locale

enum class EPriority {
    LOW,
    NORMAL,
    HIGH,
    EXTRA;

    companion object {
        fun fromString(value: String): EPriority {
            return when (value.lowercase(Locale.getDefault())) {
                "low" -> LOW
                "normal" -> NORMAL
                "high" -> HIGH
                "extra" -> EXTRA
                else -> throw ParameterNotFoundException("Enum constant for value: $value")
            }
        }
    }
}