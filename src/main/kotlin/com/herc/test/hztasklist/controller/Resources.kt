package com.herc.test.hztasklist.controller

object Resources {
    object AuthApi {
        const val ROOT = "/api/auth"
        const val SIGN_IN = "/signin"
        const val SIGN_UP = "/signup"
        const val REFRESH = "/refresh"
    }

    object TaskApi {
        const val ROOT = "/api/task"
        const val TASK_NEW = "/new"
        const val TASK_EDIT = "/edit"
        const val TASKS_LIST_WITH_STATUS = "/list"
        const val TASK_COMPLETE = "/status"
        const val TASK_DELETE = "/delete"
        const val TASKS_LIST_FOR_EVERY_USER = "/users-tasks"
    }

    object AdminApi {
        const val ROOT = "/api/admin"
        const val DELETE_USER = "/user-delete"
        const val ALL_USERS_LIST = "/users-list"
        const val GIVE_TO_USER_ROLE_ADMIN = "/user-now-admin"
        const val REMOVE_FROM_USER_ROLE_ADMIN = "/user-not-admin"
        const val ALL_USER_TASKS = "/user-tasks"
        const val DELETE_TASK = "/delete-task"
        const val EDIT_TASK = "/update-task"
        const val ADMIN_TASK_STATISTIC = "/task-stat"
        const val ADMIN_USER_STATISTIC = "/user-stat"
        const val TASKS_FILE_REPORT = "/report"
    }
}