package com.travelsmartplus.routes

import com.travelsmartplus.dao.org.OrgDAOFacadeImpl
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.Org
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.SignInRequest
import com.travelsmartplus.models.requests.SignUpRequest
import com.travelsmartplus.models.requests.UpdatePasswordRequest
import com.travelsmartplus.models.responses.AuthResponse
import com.travelsmartplus.models.responses.HttpResponses.BAD_REQUEST
import com.travelsmartplus.models.responses.HttpResponses.DUPLICATE_ORG
import com.travelsmartplus.models.responses.HttpResponses.DUPLICATE_USER
import com.travelsmartplus.models.responses.HttpResponses.FAILED_CREATE_ORG
import com.travelsmartplus.models.responses.HttpResponses.FAILED_CREATE_USER
import com.travelsmartplus.models.responses.HttpResponses.FAILED_PASSWORD_UPDATE
import com.travelsmartplus.models.responses.HttpResponses.FAILED_SIGNIN
import com.travelsmartplus.models.responses.HttpResponses.FAILED_SIGNUP
import com.travelsmartplus.models.responses.HttpResponses.INVALID_CREDENTIALS
import com.travelsmartplus.models.responses.HttpResponses.NOT_FOUND
import com.travelsmartplus.models.responses.HttpResponses.UNAUTHORIZED
import com.travelsmartplus.models.responses.UserSession
import com.travelsmartplus.utils.HashingService
import com.travelsmartplus.utils.SaltedHash
import com.travelsmartplus.utils.Validator
import com.travelsmartplus.utils.token.JwtTokenService
import com.travelsmartplus.utils.token.TokenClaim
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

/**
 * Defines the authentication routes for user sign-up, sign-in, and refresh token.
 * @author Gabriel Salas
 */

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
            if (orgDAO.getOrgByDuns(request.duns) != null)
                return@post call.respond(HttpStatusCode.Conflict, DUPLICATE_ORG)

            if (userDAO.getUserByEmail(request.email) != null)
                return@post call.respond(HttpStatusCode.Conflict, DUPLICATE_USER)

            // Create org and user with encrypted password
            val saltedHash = hashingService.generate(request.password)
            val org = Org(orgName = request.orgName, duns = request.duns)
            val orgId = orgDAO.addOrg(org)?.id ?: return@post call.respond(
                HttpStatusCode.InternalServerError,
                FAILED_CREATE_ORG
            )


            val user = User(
                orgId = orgId,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                admin = true,  // First user is always admin
                password = saltedHash.hash,
                salt = saltedHash.salt,
                accountSetup = true
            )
            userDAO.addUser(user) ?: return@post call.respond(HttpStatusCode.InternalServerError, FAILED_CREATE_USER)
            call.respond(HttpStatusCode.Created)
        } catch (e: ContentTransformationException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, FAILED_SIGNUP)
        }
    }

    post("/signin") {
        try {
            val request = call.receive<SignInRequest>()

            // Check is user exists and validate password
            val user = userDAO.getUserByEmail(request.email) ?: throw NotFoundException()

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
                val refreshToken =
                    tokenService.generate(TokenClaim("userId", user.id.toString()), expiration = 15778800000)
                call.sessions.set(UserSession(userId = user.id))
                call.respond(
                    HttpStatusCode.OK,
                    AuthResponse(token, refreshToken, user.accountSetup, user.admin, user.orgId)
                )
            } else {
                call.respond(HttpStatusCode.Unauthorized, INVALID_CREDENTIALS)
            }


        } catch (e: IllegalArgumentException) { // No details for other exceptions to avoid disclosing private data
            call.respond(HttpStatusCode.BadRequest, FAILED_SIGNIN)
        } catch (e: ContentTransformationException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
        } catch (e: NotFoundException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.NotFound, INVALID_CREDENTIALS)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, FAILED_SIGNIN)
        }
    }

    authenticate("auth-jwt") {
        // Refresh token rotation
        get("/authenticate") {
            try {

                // Extra security layer - Only allows authorisation if valid userId Cookie
                val userId = call.request.cookies["user_id"] ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    UNAUTHORIZED
                )

                // Generate new tokens
                val user = userDAO.getUser(userId.toInt()) ?: throw NotFoundException()
                val token = tokenService.generate(TokenClaim("userId", user.id.toString()), expiration = 900000)
                val refreshToken =
                    tokenService.generate(TokenClaim("userId", user.id.toString()), expiration = 15778800000)
                call.respond(HttpStatusCode.OK, AuthResponse(token, refreshToken, user.accountSetup, user.admin))

            } catch (e: NotFoundException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.NotFound, NOT_FOUND)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        // Update password
        post("/update-password") {
            try {
                val request = call.receive<UpdatePasswordRequest>()
                Validator.validateUpdatePasswordRequest(request)

                // Check user exists
                val user = userDAO.getUser(request.userId) ?: throw NotFoundException()

                // Encrypt and store password
                val saltedHash = hashingService.generate(request.newPassword)
                userDAO.updatePassword(user.id!!, saltedHash)

                call.respond(HttpStatusCode.Created)

            } catch (e: ContentTransformationException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
            } catch (e: NotFoundException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.NotFound, NOT_FOUND)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, FAILED_PASSWORD_UPDATE)
            }
        }
    }

}
