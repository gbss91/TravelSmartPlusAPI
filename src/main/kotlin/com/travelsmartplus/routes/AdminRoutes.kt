package com.travelsmartplus.routes


import com.travelsmartplus.dao.booking.BookingDAOFacadeImpl
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.AddUserRequest
import com.travelsmartplus.models.responses.HttpResponses
import com.travelsmartplus.models.responses.HttpResponses.DUPLICATE_ADD_USER
import com.travelsmartplus.models.responses.HttpResponses.FAILED_CREATE_USER
import com.travelsmartplus.models.responses.HttpResponses.INTERNAL_SERVER_ERROR
import com.travelsmartplus.utils.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines the admin routes for user management
 * @author Gabriel Salas.
 */

fun Route.adminRoutes() {
    val userDAO = UserDAOFacadeImpl()
    val bookingDAO = BookingDAOFacadeImpl()
    val hashingService = HashingService()

    // Get all users
    get("/admin/users/{orgId}") {
        try {
            val orgId = call.parameters["orgId"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
            val requesterId =
                call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString().toInt()// User ID in JWT Token
            val requester = userDAO.getUser(requesterId)

            // Check admin belongs to company
            if (requester?.orgId == orgId) {
                val allUsers = userDAO.allUsers(orgId)
                call.respond(HttpStatusCode.OK, allUsers)
            } else {
                call.respond(HttpStatusCode.Forbidden, HttpResponses.FORBIDDEN)
            }

        } catch (e: BadRequestException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, INTERNAL_SERVER_ERROR)
        }
    }

    // Create user
    post("/admin/new-user") {
        try {
            val request = call.receive<AddUserRequest>()
            val requesterId =
                call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString().toInt()// User ID in JWT Token
            val orgId = userDAO.getUser(requesterId)?.orgId ?: return@post call.respond(
                HttpStatusCode.InternalServerError,
                FAILED_CREATE_USER
            )

            // Validate duplicate user
            if (userDAO.getUserByEmail(request.email) != null) return@post call.respond(
                HttpStatusCode.Conflict,
                DUPLICATE_ADD_USER
            )


            // Create hash and user
            val saltedHash = hashingService.generate(request.password)
            val user = User(
                orgId = orgId,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                admin = request.admin,
                password = saltedHash.hash,
                salt = saltedHash.salt,
                accountSetup = false
            )
            userDAO.addUser(user) ?: return@post call.respond(HttpStatusCode.InternalServerError, FAILED_CREATE_USER)

            call.respond(HttpStatusCode.Created, user)
        } catch (e: ContentTransformationException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, FAILED_CREATE_USER)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, FAILED_CREATE_USER)
        }
    }

    // Get all company bookings
    get("/admin/bookings/{orgId}") {
        try {
            val orgId = call.parameters["orgId"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
            val requesterId =
                call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString().toInt()// User ID in JWT Token
            val requester = userDAO.getUser(requesterId)

            // Check admin belongs to company
            if (requester?.orgId == orgId) {
                val allBookings = bookingDAO.getAllBookings(orgId)
                call.respond(HttpStatusCode.OK, allBookings)
            } else {
                call.respond(HttpStatusCode.Forbidden, HttpResponses.FORBIDDEN)
            }

        } catch (e: BadRequestException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, FAILED_CREATE_USER)
        }
    }

}