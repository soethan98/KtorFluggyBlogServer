package com.example.routes

import com.example.domain.model.ApiRequest
import com.example.domain.model.Endpoint
import com.example.utils.Constants.AUDIENCE
import com.example.utils.Constants.ISSUER
import com.example.utils.TokenManager
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.routing.*
import com.google.api.client.http.javanet.NetHttpTransport
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Route.loginRoute(app:Application,tokenManager: TokenManager) {
    post(Endpoint.Login.path){
        val request = call.receive<ApiRequest>()
        if (request.tokenId.isNotEmpty()){

            val result = verifyGoogleTokenId(tokenId = request.tokenId, app =  app)

            if (result!=null){
                val  token = tokenManager.generateJwtToken(result.payload.subject, userEmail = result.payload.email)
                app.log.info("Token $token")
                call.respond(HttpStatusCode.OK, message = token)

                //call.respondRedirect(Endpoint.Authorized.path)
            }else{
               // call.respondRedirect(Endpoint.Unauthorized.path)
            }
        }
    }
}


fun verifyGoogleTokenId(tokenId:String,app: Application):GoogleIdToken?{
    return try {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(System.getenv("google_web_client")))
            .setIssuer(ISSUER)
            .build()
        app.log.info("Success ${verifier.verify(tokenId).toString()}")
        app.log.info("Success")

      verifier.verify(tokenId)
    }catch (e:Exception){

        null
    }


}