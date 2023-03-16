package com.travelsmartplus.routes

import com.travelsmartplus.dao.org.OrgDAOFacadeImpl
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.Org
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.SignInRequest
import com.travelsmartplus.models.requests.SignUpRequest
import com.travelsmartplus.models.responses.AuthResponse
import com.travelsmartplus.models.responses.UserSession
import com.travelsmartplus.utils.HashingService
import com.travelsmartplus.utils.SaltedHash
import com.travelsmartplus.utils.Validator
import com.travelsmartplus.utils.token.JwtTokenService
import com.travelsmartplus.utils.token.TokenClaim
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlin.Exception

fun Route.authRoutes() {
    val userDAO = UserDAOFacadeImpl()
    val orgDAO = OrgDAOFacadeImpl()
    val hashingService = HashingService()
    val tokenService = JwtTokenService()

    post("/signup") {
        try {
            val request = call.receive<SignUpRequest>()
            Validator.validateSignUpRequest(request)

            // Check duplicates
            if (orgDAO.getOrgByDuns(request.duns) != null) {
                call.respond(HttpStatusCode.Conflict, "Organization already exists")
                return@post
            }
            if (userDAO.getUserByEmail(request.email) != null) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
                return@post
            }

            // Create org and user with encrypted password
            val saltedHash = hashingService.generate(request.password)
            val org = Org(orgName = request.orgName, duns = request.duns)
            val orgId = orgDAO.addOrg(org)?.id ?: return@post call.respond(
                HttpStatusCode.InternalServerError,
                "Failed to create organisation"
            )

            val user = User(
                orgId = orgId,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                admin = true,  // First user is always admin
                password = saltedHash.hash,
                salt = saltedHash.salt
            )
            userDAO.addUser(user) ?: return@post call.respond(
                HttpStatusCode.InternalServerError,
                "Failed to create user"
            )
            call.respond(HttpStatusCode.Created)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, "Failed to sign up")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to sign up")
        }
    }

    post("/signin") {
        try {
            val request = call.receive<SignInRequest>()
            Validator.validateSignInRequest(request)

            // Check is user exists and validate password
            val user = userDAO.getUserByEmail(request.email) ?: return@post call.respond(HttpStatusCode.Unauthorized, "Incorrect username or password")
            val isPassValid = hashingService.verify(
                value = request.password,
                saltedHash = SaltedHash(
                    hash = user.password,
                    salt = user.salt
                )
            )

            // Create JWT tokens and session
            if (isPassValid) {
                val token = tokenService.generate(TokenClaim("userId", user.id.toString()), expiration = 900000)
                val refreshToken =  tokenService.generate(TokenClaim("userId", user.id.toString()), expiration = 15778800000)
                call.sessions.set(UserSession(userId = user.id))
                call.respond(HttpStatusCode.OK, AuthResponse(token, refreshToken))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Incorrect username or password")
            }

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, "Failed to sign in")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to sign in") // No details for other exceptions to avoid disclosing private data
        }
    }

    authenticate("auth-jwt") {
        // Refresh token rotation
        post("/authenticate") {
            try {
                // Extra security layer - Only allows authorisation if already signed in
                val userId = call.sessions.get<UserSession>()?.userId ?: return@post call.respond(HttpStatusCode.Unauthorized, "Please sign in")

                // Generate new tokens
                val user = userDAO.getUser(userId) ?: return@post call.respond(HttpStatusCode.NotFound)
                val token = tokenService.generate(TokenClaim("userId", user.id.toString()), expiration = 900000)
                val refreshToken =  tokenService.generate(TokenClaim("userId", user.id.toString()), expiration = 15778800000)
                call.respond(HttpStatusCode.OK, AuthResponse(token, refreshToken))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }

        }
    }

}
