package com.herc.test.hztasklist.model.payload.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.herc.test.hztasklist.model.EPriority
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Schema(description = "Task Request DTO")
data class TaskRequestDto(
    @NotNull(message = "Title for Task is required")
    @Schema(description = "Title for your Task", example = "Implement class User")
    val title: String?,

    @NotNull(message = "The expiration date and time for Task is required")
    @Schema(description = "Expiration date and time", example = "02/08/2024 17:34")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    val expiredTime: LocalDateTime,


    val taskPriority: EPriority,
    val userId: Long?
)
