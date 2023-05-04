package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.domain.model.User
import io.ktor.server.config.*
import java.util.*
import javax.security.auth.Subject

class TokenManager(config: HoconApplicationConfig){


    private val audience = config.property("jwt.audience").getString()
    private val secret = config.property("jwt.secret").getString()
    private val issuer = config.property("jwt.issuer").getString()
    private val expirationDate = System.currentTimeMillis() + 600000


    /***
     * Takes in the user object and returns a JWT token with username and userId embedded in it.
     */

    fun generateJwtToken(subject: String,userEmail:String):String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)

            .withClaim("userId", subject)
            .withExpiresAt(Date(expirationDate))
            .sign(Algorithm.HMAC256(secret))
    }


    /**
     * Verifies if the passed in JWT token in the request is valid or not
     */
    fun verifyJWTToken():JWTVerifier = JWT.require(Algorithm.HMAC256(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()
    }
