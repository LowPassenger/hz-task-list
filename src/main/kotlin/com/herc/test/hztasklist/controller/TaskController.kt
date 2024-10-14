package com.herc.test.hztasklist.controller

import com.herc.test.hztasklist.model.ERole
import com.herc.test.hztasklist.model.payload.dto.request.AdminChangeTaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.request.NewTaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.request.UserChangeTaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.response.TaskResponseDto
import com.herc.test.hztasklist.service.AuthenticationService
import com.herc.test.hztasklist.service.TaskService
import com.herc.test.hztasklist.service.UserService
import com.herc.test.hztasklist.service.mapper.impl.AdminChangeTaskRequestMapper
import com.herc.test.hztasklist.service.mapper.impl.NewTaskRequestDtoMapper
import com.herc.test.hztasklist.service.mapper.impl.TaskToResponseDtoMapper
import com.herc.test.hztasklist.service.mapper.impl.UserChangeTaskRequestDtoMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping(Resources.TaskApi.ROOT)
@Tag(name = "Task Controller", description = "Task API")
class TaskController {
    private val logger = LoggerFactory.getLogger(TaskController::class.java)

    @Autowired
    lateinit var taskService: TaskService

    @Autowired
    lateinit var authService: AuthenticationService

    @Autowired
    lateinit var newRequestMapper: NewTaskRequestDtoMapper

    @Autowired
    lateinit var userRequestMapper: UserChangeTaskRequestDtoMapper

    @Autowired
    lateinit var adminRequestMapper: AdminChangeTaskRequestMapper

    @PostMapping(value = [Resources.TaskApi.TASK_NEW])
    @Operation(summary = "Create new Task for User")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun createTask(request: HttpServletRequest,
                   @Valid @RequestBody taskRequest: NewTaskRequestDto) : TaskResponseDto {
        val user = authService.getUserFromHttpRequest(request)
        return taskService.createNewTask(user, taskRequest)
    }

    @GetMapping(value = [Resources.TaskApi.TASKS_LIST_FOR_EVERY_USER])
    @Operation(summary = "Returns list of all User Tasks")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun allUserTasksList(request: HttpServletRequest) : List<TaskResponseDto> {
        val user = authService.getUserFromHttpRequest(request)
        val userId = user.id!!
        return taskService.getAllTasksByUserId(userId)
    }

    @GetMapping(value = [Resources.TaskApi.TASKS_LIST_WITH_STATUS])
    @Operation(summary = "Returns list of User Tasks with chosen status")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun taskListWithStatus(request: HttpServletRequest,
                           @RequestParam("complete") complete: Boolean) : List<TaskResponseDto> {
        val user = authService.getUserFromHttpRequest(request)
        val userId = user.id!!
        return taskService.getUserTasksListWithStatus(userId, complete)
    }

    @DeleteMapping(value = [Resources.TaskApi.TASK_DELETE])
    @Operation(summary = "Delete Tasks with chosen id")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    fun deleteTask(request: HttpServletRequest,
                   @RequestParam("id") id: Long) : ResponseEntity<*> {
        val user = authService.getUserFromHttpRequest(request)
        val hasAdminRole = user.roles.any { role -> role.name == ERole.ROLE_ADMIN }
        var isTaskDeleted: Boolean = false
        if (hasAdminRole) {
            isTaskDeleted = taskService.delete(id)
        } else
            if (user.roles.any { role -> role.name == ERole.ROLE_USER}) {
                isTaskDeleted = taskService.delete(id, user)
            }
        return if (isTaskDeleted) ResponseEntity.ok("Task with $id was deleted successfully")
        else {
            logger.error("User $user has no permission to delete Task with $id")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Task $id "
                        + "wasn't deleted. See log files for details"
            )
        }
    }

    @PutMapping(value = [Resources.TaskApi.TASK_EDIT])
    @Operation(summary = "Update chosen Task")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    fun update(request: HttpServletRequest,
               @RequestParam("id") id: Long,
               @Valid @RequestBody taskRequest: Any): ResponseEntity<*> {
        val isTaskExist = taskService.existById(id)
        if (!isTaskExist) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Task with id $id not found")
        }

        val userRoles = request.isUserInRole("ROLE_ADMIN")
        val taskForUpdate = when {
            userRoles -> {
                val adminRequest = taskRequest as AdminChangeTaskRequestDto
                adminRequestMapper.toModel(adminRequest)
            }
            request.isUserInRole("ROLE_USER") -> {
                val userRequest = taskRequest as UserChangeTaskRequestDto
                userRequestMapper.toModel(userRequest)
            }
            else -> return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You do not have permission to perform this action")
        }

        taskForUpdate.id = id
        return ResponseEntity.ok(taskService.update(taskForUpdate))
    }

    @GetMapping(value = [Resources.TaskApi.TASK_COMPLETE])
    @Operation(summary = "Change Task status to 'complete'")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun changeTaskStatus(request: HttpServletRequest,
                         @RequestParam("id") id: Long) : ResponseEntity<*> {
        val user = authService.getUserFromHttpRequest(request)
        val userId = user.id!!
        val changedTask = taskService.changeTaskStatusToComplete(userId)
        return ResponseEntity.ok().body(changedTask)
    }
}