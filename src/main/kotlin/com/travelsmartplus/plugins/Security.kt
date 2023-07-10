package com.travelsmartplus.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.responses.HttpResponses.UNAUTHORIZED
import com.travelsmartplus.models.responses.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

fun Application.configureSecurity() {
    val myRealm = System.getenv("JWT_REALM")
    val secret = System.getenv("JWT_SECRET")
    val audience = System.getenv("JWT_AUDIENCE")
    val issuer = System.getenv("JWT_ISSUER")
    val dao = UserDAOFacadeImpl()

    authentication {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId") != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, UNAUTHORIZED)
            }
        }

        // Admin authorisation
        jwt("adminAuth-jwt") {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->

                val claimUser = credential.payload.getClaim("userId").asString().toInt()
                val isAdmin = dao.getUser(claimUser)?.admin

                if (isAdmin == true) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Forbidden, "Unauthorised")
            }
        }
    }

    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
}

