package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.repository.TaskRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TaskService(val taskRepository : TaskRepository) {
    private val logger = LoggerFactory.getLogger(TaskService::class.java)

    fun getById(id: Long) : Task? {
        return taskRepository.findById(id).orElseThrow {
            logger.error("Task with id $id not found!")
            ParameterNotFoundException("Task with id $id parameter not found")
        }
    }
}