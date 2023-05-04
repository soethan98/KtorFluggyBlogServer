package com.example.routes

import com.example.domain.model.ApiResponse
import com.example.domain.model.Endpoint
import com.example.domain.model.User
import com.example.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUserInfoRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate {
        get(Endpoint.GetUserInfo.path) {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asString()
            if (userId == null) {
                app.log.info("INVALID SESSION")
                call.respondRedirect(Endpoint.Unauthorized.path)
            } else {
                try {
                    call.respond(
                        message = ApiResponse<User>(
                            success = true,
                            data = userDataSource.getUserInfo(userId = userId)

                        ), status = HttpStatusCode.OK
                    )
                } catch (e: Exception) {
                    app.log.info("INVALID SESSION")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }
    }
}