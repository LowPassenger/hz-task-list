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
    }

    object UserApi{
        const val ROOT = "/api/me"
        const val FCM_TOKEN = "/fcmToken"
        const val DELETE = "/delete"
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