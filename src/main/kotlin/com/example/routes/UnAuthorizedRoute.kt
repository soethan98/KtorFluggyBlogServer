package com.example.routes

import com.example.domain.model.ApiResponse
import com.example.domain.model.Endpoint
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unAuthorizedRoute(){
    get(Endpoint.Unauthorized.path){
        call.respond(message = ApiResponse(
            success = false,
            data = null
        ),
            status = HttpStatusCode.Unauthorized)
    }


}
