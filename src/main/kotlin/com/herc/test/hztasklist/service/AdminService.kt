package com.herc.test.hztasklist.service

import com.herc.test.hztasklist.advizor.exceptions.ParameterNotFoundException
import com.herc.test.hztasklist.model.EPriority
import com.herc.test.hztasklist.model.ERole
import com.herc.test.hztasklist.model.entity.Role
import com.herc.test.hztasklist.model.payload.dto.request.AdminChangeTaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.response.TaskResponseDto
import com.herc.test.hztasklist.repository.RoleRepository
import com.herc.test.hztasklist.service.mapper.impl.TaskToResponseDtoMapper
import com.herc.test.hztasklist.util.DateTimeUtil
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AdminService {
    private val logger = LoggerFactory.getLogger(AdminService::class.java)

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var taskService: TaskService

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var taskMapper: TaskToResponseDtoMapper

    fun deleteUser(userId: Long) : Boolean {
        val user = userService.getById(userId)
        val userEmail = user.email
        userService.delete(user)
        return userService.existsByEmail(userEmail)
    }

    fun setUserAdminStatus(id: Long) : Boolean {
        val user = userService.getById(id)

        val adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
            .orElseThrow {
                logger.error("Role USER_ROLE was not found in the database")
                ParameterNotFoundException("Parameter ROLE_USER was not found in the database.")
            }

        val hasAdminRole = user.roles.any { role -> role == adminRole }
        if (hasAdminRole) return false
        var userRoles = user.roles.plus(adminRole)
        user.roles = userRoles as MutableSet<Role>
        userService.save(user)
        return true
    }

    fun deleteUserAdminStatus(id: Long) : Boolean {
        val user = userService.getById(id)

        val adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
            .orElseThrow {
                logger.error("Role USER_ROLE was not found in the database")
                ParameterNotFoundException("Parameter ROLE_USER was not found in the database.")
            }

        val hasAdminRole = user.roles.any { role -> role == adminRole }
        if (!hasAdminRole) return false
        var userRoles = user.roles.minus(adminRole)
        user.roles = userRoles as MutableSet<Role>
        userService.save(user)
        return true
    }

    fun getTasksListForUser(id: Long) : List<TaskResponseDto> {
        val user = userService.getById(id)
        return taskService.getAllTasksByUserId(id)
    }

    fun deleteTask(id: Long) : Boolean {
        return taskService.delete(id)
    }

    @Transactional
    fun updateTask(taskId: Long, requestDto: AdminChangeTaskRequestDto) : TaskResponseDto {
        var task = taskService.getById(taskId)

        task.user = userService.getById(requestDto.userId!!)
        task.taskPriority = EPriority.fromString(requestDto.taskPriority)
        task.title = requestDto.title
        task.description = requestDto.description
        task.expiredTime = DateTimeUtil.toMillis(requestDto.expiredTime)
        task.isComplete = requestDto.isComplete

        val newTask = taskService.save(task)
        return taskMapper.toDto(newTask)
    }

    fun tasksStatistic() : String {
        val completedTasks = taskService.completedTasksQuantity()
        val uncompletedTasks = taskService.uncompletedTasksQuantity()
        val request = StringBuilder()
        request.append("For now completes tasks quantity is ")
            .append(completedTasks)
            .append(System.lineSeparator())
            .append(" and uncompleted tasks quantity is ").append(uncompletedTasks)
        return request.toString()
    }

    fun usersStatistic() : String {
        val usersDtoList = userService.getAll()
        val response = StringBuilder()
        val current = StringBuilder()

        usersDtoList.forEach { user -> {
            current.setLength(0)
            current.append("User with id ")
                .append(user.id)
                .append(" has ")
                .append(user.tasks.count())
                .append(" own tasks")
                .append(System.lineSeparator())
            response.append(current)
        } }

        return response.toString()
    }

    fun generateReportFile() : ResponseEntity<ByteArrayResource> {
        val users = userService.getAll()
        val csvContent = StringBuilder()

        csvContent.append("User id,email,")
            .append("allTasks,completedTasks,uncompletedTasks,")
            .append("lastTaskId,lastTaskDate")
            .append(System.lineSeparator())

        users.forEach { user ->
            val allTasksQuantity = taskService.getAllTasksByUserId(user.id!!).count()
            val completedTasksQuantity = taskService
                .getUserTasksListWithStatus(user.id, true).count()
            val uncompletedTasksQuantity= taskService
                .getUserTasksListWithStatus(user.id, false).count()
            var lastUserTaskId: Long = 0
            var lastUserTaskDate: String = "none"
            val lastTask = taskService.getLastTaskByUserId(user.id)
            if (lastTask.isPresent) {
                lastUserTaskId = lastTask.get().id
                lastUserTaskDate = lastTask.get().timeStamp
            }

            csvContent.append("${user.id},${user.email},")
                .append("$allTasksQuantity,$completedTasksQuantity,$uncompletedTasksQuantity")
                .append("$lastUserTaskId,$lastUserTaskDate")
        }

        val resource = ByteArrayResource(csvContent.toString().toByteArray())
        val headers = HttpHeaders().apply {
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.csv")
            contentType = MediaType.APPLICATION_OCTET_STREAM
        }

        return ResponseEntity(resource, headers, HttpStatus.OK)
    }
}