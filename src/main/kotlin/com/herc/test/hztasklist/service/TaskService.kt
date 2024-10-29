package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.model.EPriority
import com.herc.test.hztasklist.model.entity.Task
import com.herc.test.hztasklist.model.entity.User
import com.herc.test.hztasklist.model.payload.dto.request.NewTaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.request.UserChangeTaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.response.TaskResponseDto
import com.herc.test.hztasklist.repository.TaskRepository
import com.herc.test.hztasklist.service.mapper.impl.NewTaskRequestDtoMapper
import com.herc.test.hztasklist.service.mapper.impl.TaskToResponseDtoMapper
import com.herc.test.hztasklist.util.DateTimeUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class TaskService(val taskRepository : TaskRepository) {
    private val logger = LoggerFactory.getLogger(TaskService::class.java)

    @Autowired
    lateinit var taskToResponseDtoMapper: TaskToResponseDtoMapper

    @Autowired
    lateinit var requestDtoMapper: NewTaskRequestDtoMapper

    fun getById(id: Long) : Task {
        return taskRepository.findById(id).orElseThrow {
            logger.error("Task with id $id not found!")
            ParameterNotFoundException("Task with id $id parameter")
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
        return !taskRepository.existsById(taskId)
    }

    fun delete(taskId: Long, user: User) : Boolean {
        val userTasks = getAllTasksByUserId(user.id!!)
        return if (!userTasks.any { task -> task.id == taskId }) false
        else delete(taskId)
    }

    fun update(taskRequest: UserChangeTaskRequestDto) : TaskResponseDto {
        val taskForUpdate = getById(taskRequest.taskId!!)

        taskRequest.apply {
            title?.let { taskForUpdate.title = it }
            description?.let { taskForUpdate.description = it }
            expiredTime.let { taskForUpdate.expiredTime = DateTimeUtil.toMillis(it) }
            taskPriority.let { taskForUpdate.taskPriority = EPriority.fromString(it) }
            isComplete.let { taskForUpdate.isComplete = it }
        }

        val savedTask = save(taskForUpdate)
        return taskToResponseDtoMapper.toDto(savedTask)
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

    fun changeTaskStatusToComplete(user: User, taskId: Long) : Boolean {
        val taskToChange = getById(taskId)
        if (taskToChange.user!!.id != user.id) {
            logger.error("Task with id $taskId do not belong to user with email ${user.email}")
            throw ParameterNotFoundException("Task with id $taskId in user's Task list")
        }
        if (taskToChange.isComplete == true) {
            logger.warn("Task with id $taskId already has status isComplete = true")
            return false
        }
        taskToChange.isComplete = true
        val changedTask = save(taskToChange)
        return (changedTask.isComplete == true)
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

    fun isTaskBelongToUser(user: User, taskId: Long) : Boolean {
        val userTasks = getAllTasksByUserId(user.id!!)
        return userTasks.any{it.id == taskId}
    }
}