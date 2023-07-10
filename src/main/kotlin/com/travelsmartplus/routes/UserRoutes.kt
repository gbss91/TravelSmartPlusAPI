package com.travelsmartplus.routes

import com.travelsmartplus.dao.user.TravelDataDAOFacadeImpl
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.TravelData
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.SetupAccountRequest
import com.travelsmartplus.models.responses.HttpResponses.BAD_REQUEST
import com.travelsmartplus.models.responses.HttpResponses.FAILED_DELETE_USER
import com.travelsmartplus.models.responses.HttpResponses.FAILED_EDIT_USER
import com.travelsmartplus.models.responses.HttpResponses.FAILED_SETUP
import com.travelsmartplus.models.responses.HttpResponses.FORBIDDEN
import com.travelsmartplus.models.responses.HttpResponses.INTERNAL_SERVER_ERROR
import com.travelsmartplus.models.responses.HttpResponses.NOT_FOUND
import com.travelsmartplus.utils.HashingService
import com.travelsmartplus.utils.Validator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines the general user routes to get, update and delete orgs. Add users can only be done by admin in [Route.adminRoutes]
 * @author Gabriel Salas
 */

val userDao = UserDAOFacadeImpl()
val travelDao = TravelDataDAOFacadeImpl()

fun Route.userRoutes() {

    val hashingService = HashingService()

    route("/user/{id}") {
        // Get user
        get {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
                val requesterId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
                    .toInt()// User ID in JWT Token

                // Check if user is owner or admin belongs to the same org
                if (requesterId == id || isAdminSameOrg(requesterId, id)) {
                    val user = userDao.getUser(id) ?: throw NotFoundException()
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.Forbidden, FORBIDDEN)
                }

            } catch (e: BadRequestException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
            } catch (e: NotFoundException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.NotFound, NOT_FOUND)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, INTERNAL_SERVER_ERROR)
            }
        }

        // Update user
        put {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
                val requesterId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
                    .toInt()// User ID in JWT Token
                val editedUser = call.receive<User>()

                // Check if user is owner or admin belongs to the same org
                if (requesterId == id || isAdminSameOrg(requesterId, id)) {
                    val user = userDao.editUser(id, editedUser)

                    // Add travel data if available
                    user.travelData = addEditTravelData(editedUser)

                    call.respond(HttpStatusCode.Created, user)
                } else {
                    call.respond(HttpStatusCode.Forbidden, FORBIDDEN)
                }

            } catch (e: BadRequestException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
            } catch (e: ContentTransformationException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
            } catch (e: NotFoundException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.NotFound, NOT_FOUND)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, FAILED_EDIT_USER)
            }
        }

        // Delete user
        delete {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
                val requesterId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
                    .toInt()// User ID in JWT Token

                // Check if user is owner or admin belongs to the same org
                if (requesterId == id || isAdminSameOrg(requesterId, id)) {
                    userDao.deleteUser(id)
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Forbidden, FORBIDDEN)
                }

            } catch (e: BadRequestException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
            } catch (e: NotFoundException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.NotFound, NOT_FOUND)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, FAILED_DELETE_USER)
            }
        }
    }

    // Setup user account
    post("/user/{id}/setup") {
        try {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
            val request = call.receive<SetupAccountRequest>()
            Validator.validateSetupAccountRequest(request)

            // Check if user exists
            val user = userDao.getUser(id) ?: throw NotFoundException()

            // Encrypt and store password
            val saltedHash = hashingService.generate(request.newPassword)
            userDao.updatePassword(id, saltedHash)

            // Update user preferences and account setup
            user.preferredAirlines = request.preferredAirlines
            user.preferredHotelChains = request.preferredHotelChains
            user.accountSetup = true
            userDao.editUser(id, user)

            call.respond(HttpStatusCode.Created)
        } catch (e: BadRequestException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
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
            call.respond(HttpStatusCode.InternalServerError, FAILED_SETUP)
        }
    }
}

// Check if the user is an admin in the same organization
private suspend fun isAdminSameOrg(requesterId: Int, userId: Int): Boolean {
    val requester = userDao.getUser(requesterId)
    val user = userDao.getUser(userId) ?: throw NotFoundException()
    return (requester?.admin == true) && (requester.orgId == user.orgId)
}

// Add or edit travel data accordingly
private suspend fun addEditTravelData(user: User): TravelData? {
    var travelData = user.travelData
    val userId = user.id ?: throw BadRequestException("Missing parameter")

    if (travelData != null) {

        // Check if travel data already exists for this user
        val exitingTravelData = travelDao.getTravelData(userId)
        if (exitingTravelData == null) {
            travelData = travelDao.addTravelData(travelData) // Add new data
        } else {
            travelData = travelDao.editTravelData(userId, travelData) // Edit existing data
        }
    }
    return travelData
}

