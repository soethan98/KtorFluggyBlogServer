package com.example.plugins

import com.example.routes.authorizedRoute
import com.example.routes.loginRoute
import com.example.routes.unAuthorizedRoute
import com.example.utils.TokenManager
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import org.koin.java.KoinJavaComponent
import org.koin.ktor.ext.inject
import rootRoute

fun Application.configureRouting() {
    routing {
        val tokenManager: TokenManager by
        KoinJavaComponent.inject(TokenManager::class.java)

        rootRoute()
        loginRoute(application,tokenManager)
        unAuthorizedRoute()
        authorizedRoute()
    }
}
