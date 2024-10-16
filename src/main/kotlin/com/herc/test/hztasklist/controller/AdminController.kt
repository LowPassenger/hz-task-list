package com.herc.test.hztasklist.controller

import com.herc.test.hztasklist.model.payload.dto.request.AdminChangeTaskRequestDto
import com.herc.test.hztasklist.service.AdminService
import com.herc.test.hztasklist.service.AuthenticationService
import com.herc.test.hztasklist.service.UserService
import com.herc.test.hztasklist.service.mapper.impl.AuthenticationResponseDtoMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping(Resources.AdminApi.ROOT)
@Tag(name = "Admin Controller", description = "Task API")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class AdminController {
    private val logger = LoggerFactory.getLogger(AdminController::class.java)

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var authService: AuthenticationService

    @Autowired
    lateinit var adminService: AdminService

    @Autowired
    lateinit var mapper: AuthenticationResponseDtoMapper

    @GetMapping(value = [Resources.AdminApi.ALL_USERS_LIST])
    @Operation(summary = "Return list of all User")
    fun getAllUsers() : ResponseEntity<*> {
        return ResponseEntity.ok().body(userService.getAll())
    }

    @GetMapping(value = [Resources.AdminApi.ALL_USER_TASKS])
    @Operation(summary = "Return list of all Tasks for User with chosen id")
    fun getUserTasksList(@RequestParam("id") id: Long) : ResponseEntity<*> {
        return ResponseEntity.ok().body(adminService.getTasksListForUser(id))
    }

    @DeleteMapping(value = [Resources.AdminApi.DELETE_USER])
    @Operation(summary = "Delete User with chosen id")
    fun deleteUser(request: HttpServletRequest,
                   @RequestParam("id") id: Long) : ResponseEntity<*> {
        val user = authService.getUserFromHttpRequest(request)
        if (id == user.id) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                .body("Suicide is deprecated in this version of the application")
        }

        if (adminService.deleteUser(id)) {
            logger.info("User with id $id was deleted")
            return ResponseEntity.ok().body("User with id $id successfully deleted")
        }
        else {
            logger.error("User with id $id wasn't deleted. Wrong id?")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("An error occurred while deleting user with id $id")
        }
    }

    @GetMapping(value = [Resources.AdminApi.GIVE_TO_USER_ROLE_ADMIN])
    @Operation(summary = "Set Admin status to User with chosen id ")
    fun getUserStatusAdmin(@RequestParam("id") id: Long) : ResponseEntity<*> {
        return if (adminService.setUserAdminStatus(id))
            ResponseEntity.ok().body("User with id $id now has Admin status")
        else
            ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body("User with id $id already has an Admin status")
    }

    @GetMapping(value = [Resources.AdminApi.REMOVE_FROM_USER_ROLE_ADMIN])
    @Operation(summary = "Remove Admin status from User with chosen id ")
    fun removeUserStatusAdmin(@RequestParam("id") id: Long) : ResponseEntity<*> {
        return if (adminService.deleteUserAdminStatus(id))
            ResponseEntity.ok().body("User with id $id now has no Admin status")
        else
            ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body("User with id $id never has an Admin status")
    }

    @DeleteMapping(value = [Resources.AdminApi.DELETE_TASK])
    @Operation(summary = "Delete Task with chosen id ")
    fun deleteTask(@RequestParam("id") taskId: Long) : ResponseEntity<*> {
        if (adminService.deleteTask(taskId)) {
            logger.info("Task with id $taskId was deleted")
            return ResponseEntity.ok().body("Task with id $taskId was successfully deleted")
        }
        else {
            logger.error("Task with id $taskId wasn't deleted. Wrong id?")
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Task with id $taskId wasn't deleted")
        }
    }

    @PutMapping(value = [Resources.AdminApi.EDIT_TASK])
    @Operation(summary = "Update chosen Task")
    fun updateTask(@RequestParam("id") id: Long,
                   @Valid @RequestBody taskRequest: AdminChangeTaskRequestDto)
    : ResponseEntity<*> {
        return ResponseEntity.ok().body(adminService.updateTask(id, taskRequest))
    }

    @GetMapping(value = [Resources.AdminApi.ADMIN_TASK_STATISTIC])
    @Operation(summary = "Show all completed and uncompleted tasks quantity")
    fun tasksStatistic() : ResponseEntity<*> {
        return ResponseEntity.ok().body(adminService.tasksStatistic())
    }

    @GetMapping(value = [Resources.AdminApi.ADMIN_USER_STATISTIC])
    @Operation(summary = "Show current tasks quantity for every user")
    fun usersStatistic() : ResponseEntity<*> {
        return ResponseEntity.ok().body(adminService.usersStatistic())
    }

    @GetMapping(value = [Resources.AdminApi.TASKS_FILE_REPORT])
    @Operation(summary = "Return file report.csv contains statistic for all users")
    fun generateCsvReport(): ResponseEntity<ByteArrayResource> {
        return adminService.generateReportFile()
    }
}