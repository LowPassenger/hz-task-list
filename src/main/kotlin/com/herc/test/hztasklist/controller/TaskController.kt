package com.herc.test.hztasklist.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.herc.test.hztasklist.model.ERole
import com.herc.test.hztasklist.model.payload.dto.request.AdminChangeTaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.request.NewTaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.request.UserChangeTaskRequestDto
import com.herc.test.hztasklist.model.payload.dto.response.TaskResponseDto
import com.herc.test.hztasklist.security.services.UserDetailsImpl
import com.herc.test.hztasklist.service.TaskService
import com.herc.test.hztasklist.service.mapper.impl.AdminChangeTaskRequestMapper
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
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping(Resources.TaskApi.ROOT)
@Tag(name = "Task Controller", description = "Task API")
class TaskController {
    private val logger = LoggerFactory.getLogger(TaskController::class.java)

    @Autowired
    lateinit var taskService: TaskService

    @Autowired
    lateinit var userRequestMapper: UserChangeTaskRequestDtoMapper

    @Autowired
    lateinit var adminRequestMapper: AdminChangeTaskRequestMapper

    @PostMapping(value = [Resources.TaskApi.TASK_NEW])
    @Operation(summary = "Create new Task for User")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun createTask(@AuthenticationPrincipal userDetails: UserDetailsImpl,
                   @Valid @RequestBody taskRequest: NewTaskRequestDto) : ResponseEntity<*> {
        val response = taskService.createNewTask(userDetails.getUserFromDetails(), taskRequest)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping(value = [Resources.TaskApi.TASKS_LIST_FOR_EVERY_USER])
    @Operation(summary = "Returns list of all User Tasks")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun allUserTasksList(@AuthenticationPrincipal userDetails: UserDetailsImpl) :
            ResponseEntity<*> {
        val taskList = taskService.getAllTasksByUserId(userDetails.id!!)
        return ResponseEntity.ok().body(taskList)
    }

    @GetMapping(value = [Resources.TaskApi.TASKS_LIST_WITH_STATUS])
    @Operation(summary = "Returns list of User Tasks with chosen status")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun taskListWithStatus(@AuthenticationPrincipal userDetails: UserDetailsImpl,
                           @RequestParam("complete") complete: Boolean) : ResponseEntity<*> {
        val taskList = taskService.getUserTasksListWithStatus(userDetails.id!!, complete)
        return ResponseEntity.ok().body(taskList)
    }

    @DeleteMapping(value = [Resources.TaskApi.TASK_DELETE])
    @Operation(summary = "Delete Tasks with chosen id")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    fun deleteTask(@AuthenticationPrincipal userDetails: UserDetailsImpl,
                   @RequestParam("id") id: Long) : ResponseEntity<*> {
        val user = userDetails.getUserFromDetails()
        val hasAdminRole = user.roles.any { role -> role.name == ERole.ROLE_ADMIN }
        var isTaskDeleted = false
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
    fun update(@AuthenticationPrincipal userDetails: UserDetailsImpl,
               @Valid @RequestBody taskRequest: UserChangeTaskRequestDto): ResponseEntity<*> {
        val user = userDetails.getUserFromDetails()
        val taskId = taskRequest.taskId
        val hasAdminRole = user.roles.any { role -> role.name == ERole.ROLE_ADMIN }
        if (hasAdminRole) {
            logger.warn("User with id ${user.id} has status ADMIN and must use "
                    + "${Resources.AdminApi.EDIT_TASK} endpoint")
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("ADMIN must use specific section endpoints")
        }

        val hasUserRole = user.roles.any { role -> role.name == ERole.ROLE_USER }
        if (!hasUserRole) {
            logger.warn("User with id ${user.id} has no status USER and  "
                    + "has no permission to change task $taskId")
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("ADMIN must use specific section endpoints")
        }

        if (!taskService.isTaskBelongToUser(user, taskId!!)) {
            logger.warn("User with id ${user.id} has no task with $taskId  "
                    + "in Task list, so task $taskId has no changes")
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Task with $taskId do not belong to user with is ${user.id}")
        }

        return ResponseEntity.ok(taskService.update(taskRequest))
    }

    @GetMapping(value = [Resources.TaskApi.TASK_COMPLETE])
    @Operation(summary = "Change Task status to 'complete'")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun changeTaskStatus(@AuthenticationPrincipal userDetails: UserDetailsImpl,
                         @RequestParam("id") id: Long) : ResponseEntity<*> {
        val changedTask = taskService.changeTaskStatusToComplete(userDetails.user, id)
        return if (changedTask == true) {
            ResponseEntity.ok().body("Task with id $id has complete status now")
        } else ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("There is a problem to change status for Task with id $id. See log file for details")
    }
}