package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.utils.TokenManager
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt {
            val jwtAudience = this@configureAuthentication.environment.config.property("jwt.audience").getString()
            realm = this@configureAuthentication.environment.config.property("jwt.realm").getString()
            val algoSecret = this@configureAuthentication.environment.config.property("jwt.secret").getString()
            val issuer= this@configureAuthentication.environment.config.property("jwt.issuer").getString()
            verifier(
                JWT
                        .require(Algorithm.HMAC256(algoSecret))
                        .withAudience(jwtAudience)
                        .withIssuer(issuer)
                        .build()
            )
            validate { credential ->
                    if (credential.payload.getClaim("email").asString().isNotEmpty()) JWTPrincipal(credential.payload) else null
                }
        }
    }
}




//authentication {
//            jwt {
//                val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
//                realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
//                verifier(
//                    JWT
//                        .require(Algorithm.HMAC256("secret"))
//                        .withAudience(jwtAudience)
//                        .withIssuer(this@configureSecurity.environment.config.property("jwt.domain").getString())
//                        .build()
//                )
//                validate { credential ->
//                    if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
//                }
//            }
//        }