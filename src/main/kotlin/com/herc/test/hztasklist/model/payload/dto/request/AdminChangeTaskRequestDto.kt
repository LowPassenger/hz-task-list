package com.herc.test.hztasklist.model.payload.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

data class AdminChangeTaskRequestDto(
    @NotNull(message = "Task id is required")
    @Schema(description = "Task id", example = "1")
    val taskId: Long?,

    @Schema(description = "User id", example = "1")
    val userId: Long?,

    @Length(max = 80, message = "Task title must contains less or equals than 80 characters")
    @Schema(description = "Title for your Task", example = "Implement class User")
    val title: String?,

    @NotNull(message = "Task description is required")
    @Length(max = 300, message = "Task description must contains "
            + "less or equals than 300 characters")
    @Schema(description = "Description for your Task", example = "Add all required fields and functions")
    val description: String?,

    @NotNull(message = "The expiration date and time for Task is required")
    @Schema(description = "Expiration date and time", example = "02/08/2024 17:34")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    val expiredTime: LocalDateTime,

    @NotNull(message = "Task priority is required")
    @Schema(description = "There are some cases available for Task priority: "
            + "Low, Normal, High, Extra", example = "Normal")
    val taskPriority: String,

    @Schema(description = "Shows current Task completeness.May be 'true' or 'false'",
        example = "false")
    val isComplete: Boolean
)