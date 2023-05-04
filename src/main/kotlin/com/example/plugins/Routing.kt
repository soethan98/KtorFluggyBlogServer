package com.example.plugins

import com.example.domain.repository.UserDataSource
import com.example.routes.*
import com.example.utils.TokenManager
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import org.koin.java.KoinJavaComponent
import org.koin.ktor.ext.inject
import rootRoute

fun Application.configureRouting() {
    val userDataSource by inject<UserDataSource>()
    val tokenManager by inject<TokenManager>()
    routing {
        rootRoute()
        loginRoute(application,tokenManager, userDataSource = userDataSource)
        unAuthorizedRoute()
        authorizedRoute()
        getUserInfoRoute(application,userDataSource = userDataSource)
        updateUserRoute(application,userDataSource = userDataSource)
    }
}
