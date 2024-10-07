package com.herc.test.hztasklist.controller

object Resources {
    object AuthApi {
        const val ROOT = "/api/auth"
        const val REFRESH = "/refresh"
        const val SIGN_IN = "/signin"
        const val SIGN_UP = "/signup"
    }

    object AdminApi {
        const val ROOT = "/rm-admin"
        const val LOGIN = "/login"
        const val ALL_USERS_LIST = "/users-list"
        const val DELETE_USER = "/user-delete"
        const val CHANGE_USER_ROLE = "/user-role"
        const val ALL_USER_TASKS = "/user-tasks"
        const val TASK_EDIT = "/task-edit"
        const val TASKS_LIST_WITH_COMPLETE_STATUS = "/tasks"
        const val TASKS_LIST_FOR_EVERY_USER = "/users-tasks"
        const val TASKS_FILE_REPORT = "/report"
    }

    object UserApi{
        const val ROOT = "/api/me"
        const val FCM_TOKEN = "/fcmToken"
        const val TASK_NEW = "/task-new"
        const val TASK_EDIT = "/task-edit"
        const val TASKS_LIST_WITH_STATUS = "/task-list"
        const val TASK_COMPLETE = "/task-ok"
    }

    object Static {
        object OpenApi {
            const val ROOT_PATH = "/swagger-ui"
            const val DOCS_PATH = "/v3/api-docs"
        }

        object Admin {
            const val LOGIN = "admin/login"
        }
    }
}