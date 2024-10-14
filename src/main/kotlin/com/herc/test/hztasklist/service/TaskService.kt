package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.request.NewTaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.response.TaskResponseDto
import com.herc.test.hztasklist.repository.TaskRepository
import com.herc.test.hztasklist.service.mapper.impl.NewTaskRequestDtoMapper
import com.herc.test.hztasklist.service.mapper.impl.TaskToResponseDtoMapper
import com.herc.test.hztasklist.util.DateTimeUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional

@Service
class TaskService(val taskRepository : TaskRepository) {
    private val logger = LoggerFactory.getLogger(TaskService::class.java)

    @Autowired
    lateinit var taskToResponseDtoMapper: TaskToResponseDtoMapper

    @Autowired
    lateinit var requestDtoMapper: NewTaskRequestDtoMapper

    @Autowired
    lateinit var userService: UserService

    fun getById(id: Long) : Task {
        return taskRepository.findById(id).orElseThrow {
            logger.error("Task with id $id not found!")
            ParameterNotFoundException("Task with id $id parameter not found")
        }
    }

    fun existById(id: Long) : Boolean {
        return taskRepository.existsById(id)
    }

    fun save(task: Task) : Task {
        return taskRepository.save(task)
    }

    fun createNewTask(user: User, taskDto: NewTaskRequestDto): TaskResponseDto {
        val taskToSave = requestDtoMapper.toModel(taskDto)
        taskToSave.user = user
        val savedTask = save(taskToSave)
        return taskToResponseDtoMapper.toDto(savedTask)
    }

    fun delete(taskId: Long) : Boolean {
        val task = getById(taskId)
        taskRepository.delete(task)
        val user = task.user
        val userTasks = user!!.tasks
        userTasks.minus(task)
        user.tasks = userTasks
        userService.save(user)
        return taskRepository.existsById(taskId)
    }

    fun delete(taskId: Long, user: User) : Boolean {
        val userTasks = getAllTasksByUserId(user.id!!)
        return if (!userTasks.any { task -> task.id == taskId }) false
        else delete(taskId)
    }

    fun update(task: Task) : TaskResponseDto {
        val taskForUpdate = save(task)
        return taskToResponseDtoMapper.toDto(taskForUpdate)
    }

    fun getAllTasksByUserId(userId: Long) : List<TaskResponseDto> {
        val userTasksList = taskRepository.findAllByUserId(userId)
        return userTasksList.map {task -> taskToResponseDtoMapper.toDto(task)}
    }

    fun getUserTasksListWithStatus(userId: Long, complete: Boolean) : List<TaskResponseDto> {
        val userTasksList = if (complete == true) taskRepository.findCompletedTasksByUserId(userId)
        else taskRepository.findUncompletedTasksByUserId(userId)
        return userTasksList.map {task -> taskToResponseDtoMapper.toDto(task)}
    }

    fun changeTaskStatusToComplete(id: Long) : TaskResponseDto {
        val taskToChange = getById(id)
        taskToChange.isComplete = true
        return taskToResponseDtoMapper.toDto(taskToChange)
    }

    fun completedTasksQuantity() : Int {
        val completedTasks = taskRepository.countByIsCompleteTrue()
        return completedTasks.toInt()
    }

    fun uncompletedTasksQuantity() : Int {
        val completedTasks = taskRepository.countByIsCompleteFalse()
        return completedTasks.toInt()
    }

    fun getLastTaskByUserId(id: Long) : Optional<TaskResponseDto> {
        var lastTask: TaskResponseDto? = null
        var lastTaskDate = LocalDateTime.MIN

        val userTasks = getAllTasksByUserId(id)
        userTasks.forEach { task ->
            val currentTaskDate = LocalDateTime.parse(task.timeStamp,
                DateTimeUtil.getDateTimeFormatter())
            if (currentTaskDate.isAfter(lastTaskDate)) {
                lastTask = task
                lastTaskDate = currentTaskDate
            }
        }

        return Optional.ofNullable(lastTask)
    }
}