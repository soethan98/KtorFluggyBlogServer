package com.example.routes

import com.example.domain.model.ApiResponse
import com.example.domain.model.Endpoint
import com.example.domain.model.UserUpdate
import com.example.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Route.updateUserRoute(app:Application,
                          userDataSource: UserDataSource){
    authenticate {
        put(Endpoint.UpdateUserInfo.path) {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asString()
            val userUpdate = call.receive<UserUpdate>()
            if(userId == null) {
                app.log.info("INVALID SESSION")
                call.respondRedirect(Endpoint.Unauthorized.path)
            }else{
                try {
                    updateUserInfo(
                        app = app,
                        userId = userId,
                        userUpdate = userUpdate.copy(updatedAt = System.currentTimeMillis()),
                        userDataSource = userDataSource
                    )
                } catch (e: Exception) {
                    app.log.info("UPDATE USER INFO ERROR: $e")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }

    }
}

private suspend fun PipelineContext<Unit,ApplicationCall>.updateUserInfo(
    app: Application,
    userId: String,
    userUpdate: UserUpdate,
    userDataSource: UserDataSource
){
val response = userDataSource.updateUserInfo(userId = userId,
    firstName = userUpdate.firstName,
    lastName = userUpdate.lastName,
    updatedAt = userUpdate.updatedAt)
    if (response) {
        app.log.info("USER SUCCESSFULLY UPDATED")
        call.respond(
            message = ApiResponse(
                success = true,
                data ="Successfully Updated!"
            ),
            status = HttpStatusCode.OK
        )
    }else{
        app.log.info("ERROR UPDATING THE USER")
        call.respond(
            message = ApiResponse(success = false, data = null),
            status = HttpStatusCode.BadRequest
        )
    }
}