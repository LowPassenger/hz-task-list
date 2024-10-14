package com.herc.test.hztasklist.model.payload.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

@Schema(description = "Task Request DTO")
data class NewTaskRequestDto(
    @NotNull(message = "Title for Task is required")
    @Length(max = 80, message = "Task title must contains less or equals than 80 characters")
    @Schema(description = "Title for your Task", example = "Implement class User")
    val title: String?,

    @NotNull(message = "Task description is required")
    @Length(max = 300, message = "Task description must contains "
            + "less or equals than 300 characters")
    @Schema(description = "Description for your Task",
        example = "Add all required fields and functions")
    val description: String?,

    @NotNull(message = "The expiration date and time for Task is required")
    @Schema(description = "Expiration date and time", example = "02/08/2024 17:34")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    val expiredTime: LocalDateTime,

    @Schema(description = "There are some cases available for Task priority: "
            + "'Low', 'Normal', 'High' or 'Extra'. Default value is 'Normal'", example = "Normal")
    val taskPriority: String
)
