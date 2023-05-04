package com.example.routes

import com.example.domain.model.ApiRequest
import com.example.domain.model.Endpoint
import com.example.domain.model.User
import com.example.domain.repository.UserDataSource
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
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*
import javax.xml.crypto.Data

fun Route.loginRoute(app:Application,tokenManager: TokenManager,
                     userDataSource: UserDataSource) {
    post(Endpoint.Login.path){
        val request = call.receive<ApiRequest>()
        if (request.tokenId.isNotEmpty()){

            val result = verifyGoogleTokenId(tokenId = request.tokenId)

            if (result!=null){
                val token = tokenManager.generateJwtToken(result.payload.subject, userEmail = result.payload.email)
                call.respond(token)
                saveUserToDatabase(
                    app = app,
                    result = result,
                    userDataSource = userDataSource
                )
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.saveUserToDatabase(app: Application, result:GoogleIdToken, userDataSource: UserDataSource){
    val sub = result.payload["sub"].toString()
    val name = result.payload["name"].toString()
    val emailAddress = result.payload["email"].toString()
    val profilePhoto = result.payload["picture"].toString()
    val firstName = result.payload["given_name"].toString()
    val lastName = result.payload["family_name"].toString()

    val user = User(id = sub,
        lastName = lastName,
        firstName = firstName,
        fullName = name,
        photo = profilePhoto,
        email = emailAddress,
        createdAt = System.currentTimeMillis())

    val response = userDataSource.saveUserInfo(user)
    if (response) {
        app.log.info("USER SUCCESSFULLY SAVED/RETRIEVED")
    } else {
        app.log.info("ERROR SAVING THE USER")
        call.respond("Failed Saved")

    }

}


fun verifyGoogleTokenId(tokenId:String):GoogleIdToken?{
    return try {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(System.getenv("google_web_client")))
            .setIssuer(ISSUER)
            .build()
      verifier.verify(tokenId)
    }catch (e:Exception){
        null
    }


}