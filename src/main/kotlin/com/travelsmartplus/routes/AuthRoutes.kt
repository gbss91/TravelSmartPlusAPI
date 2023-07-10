package com.travelsmartplus.routes

import com.travelsmartplus.dao.org.OrgDAOFacadeImpl
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.Org
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.SignInRequest
import com.travelsmartplus.models.requests.SignUpRequest
<<<<<<< HEAD
import com.travelsmartplus.models.responses.AuthResponse
=======
import com.travelsmartplus.models.requests.UpdatePasswordRequest
import com.travelsmartplus.models.responses.AuthResponse
import com.travelsmartplus.models.responses.HttpResponses.BAD_REQUEST
>>>>>>> development
import com.travelsmartplus.models.responses.HttpResponses.DUPLICATE_ORG
import com.travelsmartplus.models.responses.HttpResponses.DUPLICATE_USER
import com.travelsmartplus.models.responses.HttpResponses.FAILED_CREATE_ORG
import com.travelsmartplus.models.responses.HttpResponses.FAILED_CREATE_USER
<<<<<<< HEAD
import com.travelsmartplus.models.responses.HttpResponses.FAILED_SIGNIN
import com.travelsmartplus.models.responses.HttpResponses.FAILED_SIGNUP
import com.travelsmartplus.models.responses.HttpResponses.INVALID_CREDENTIALS
=======
import com.travelsmartplus.models.responses.HttpResponses.FAILED_PASSWORD_UPDATE
import com.travelsmartplus.models.responses.HttpResponses.FAILED_SIGNIN
import com.travelsmartplus.models.responses.HttpResponses.FAILED_SIGNUP
import com.travelsmartplus.models.responses.HttpResponses.INVALID_CREDENTIALS
import com.travelsmartplus.models.responses.HttpResponses.NOT_FOUND
>>>>>>> development
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
<<<<<<< HEAD
=======
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentTransformationException
>>>>>>> development
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
<<<<<<< HEAD
import kotlin.Exception
=======

/**
 * Defines the authentication routes for user sign-up, sign-in, and refresh token.
 * @author Gabriel Salas
 */
>>>>>>> development

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
<<<<<<< HEAD
            if (orgDAO.getOrgByDuns(request.duns) != null) {
                call.respond(HttpStatusCode.Conflict, DUPLICATE_ORG)
                return@post
            }
            if (userDAO.getUserByEmail(request.email) != null) {
                call.respond(HttpStatusCode.Conflict, DUPLICATE_USER)
                return@post
            }
=======
            if (orgDAO.getOrgByDuns(request.duns) != null)
                return@post call.respond(HttpStatusCode.Conflict, DUPLICATE_ORG)

            if (userDAO.getUserByEmail(request.email) != null)
                return@post call.respond(HttpStatusCode.Conflict, DUPLICATE_USER)
>>>>>>> development

            // Create org and user with encrypted password
            val saltedHash = hashingService.generate(request.password)
            val org = Org(orgName = request.orgName, duns = request.duns)
<<<<<<< HEAD
            val orgId = orgDAO.addOrg(org)?.id ?: return@post call.respond(HttpStatusCode.InternalServerError, FAILED_CREATE_ORG)
=======
            val orgId = orgDAO.addOrg(org)?.id ?: return@post call.respond(
                HttpStatusCode.InternalServerError,
                FAILED_CREATE_ORG
            )
>>>>>>> development

            val user = User(
                orgId = orgId,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                admin = true,  // First user is always admin
                password = saltedHash.hash,
<<<<<<< HEAD
                salt = saltedHash.salt
            )
            userDAO.addUser(user) ?: return@post call.respond(HttpStatusCode.InternalServerError, FAILED_CREATE_USER)
            call.respond(HttpStatusCode.Created)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, FAILED_SIGNUP)
        } catch (e: Exception) {
=======
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
>>>>>>> development
            call.respond(HttpStatusCode.InternalServerError, FAILED_SIGNUP)
        }
    }

    post("/signin") {
        try {
            val request = call.receive<SignInRequest>()
<<<<<<< HEAD
            Validator.validateSignInRequest(request)

            // Check is user exists and validate password
            val user = userDAO.getUserByEmail(request.email) ?: return@post call.respond(
                HttpStatusCode.Unauthorized,
                INVALID_CREDENTIALS
            )
=======

            // Check is user exists and validate password
            val user = userDAO.getUserByEmail(request.email) ?: throw NotFoundException()
>>>>>>> development
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
<<<<<<< HEAD
                call.respond(HttpStatusCode.OK, AuthResponse(token, refreshToken))
=======
                call.respond(
                    HttpStatusCode.OK,
                    AuthResponse(token, refreshToken, user.accountSetup, user.admin, user.orgId)
                )
>>>>>>> development
            } else {
                call.respond(HttpStatusCode.Unauthorized, INVALID_CREDENTIALS)
            }

<<<<<<< HEAD
        } catch (e: IllegalArgumentException) { // No details for other exceptions to avoid disclosing private data
            call.respond(HttpStatusCode.BadRequest, FAILED_SIGNIN)
        } catch (e: Exception) {
=======
        } catch (e: ContentTransformationException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
        } catch (e: NotFoundException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.NotFound, INVALID_CREDENTIALS)
        } catch (e: Exception) {
            e.printStackTrace()
>>>>>>> development
            call.respond(HttpStatusCode.InternalServerError, FAILED_SIGNIN)
        }
    }

    authenticate("auth-jwt") {
        // Refresh token rotation
        get("/authenticate") {
            try {
<<<<<<< HEAD
                // Extra security layer - Only allows authorisation if already signed in
                val userId = call.sessions.get<UserSession>()?.userId ?: return@get call.respond(
=======
                // Extra security layer - Only allows authorisation if valid userId Cookie
                val userId = call.request.cookies["user_id"] ?: return@get call.respond(
>>>>>>> development
                    HttpStatusCode.Unauthorized,
                    UNAUTHORIZED
                )

                // Generate new tokens
<<<<<<< HEAD
                val user = userDAO.getUser(userId) ?: return@get call.respond(HttpStatusCode.NotFound)
                val token = tokenService.generate(TokenClaim("userId", user.id.toString()), expiration = 900000)
                val refreshToken =
                    tokenService.generate(TokenClaim("userId", user.id.toString()), expiration = 15778800000)
                call.respond(HttpStatusCode.OK, AuthResponse(token, refreshToken))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }

=======
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
>>>>>>> development
        }
    }

}
