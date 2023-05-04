package com.example.routes

import com.example.domain.model.ApiResponse
import com.example.domain.model.Endpoint
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.authorizedRoute() {
    authenticate {
        get(Endpoint.Authorized.path) {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("email")?.asString()
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
            call.respond(
                message = ApiResponse(
                    success = true,
                    data = "Authenticated"
                ),
                status = HttpStatusCode.OK
            )
        }
    }


}

