package com.travelsmartplus.routes

import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.SetupAccountRequest
import com.travelsmartplus.models.responses.HttpResponses.BAD_REQUEST
import com.travelsmartplus.models.responses.HttpResponses.FAILED_DELETE_USER
import com.travelsmartplus.models.responses.HttpResponses.FAILED_EDIT_USER
import com.travelsmartplus.models.responses.HttpResponses.FAILED_SETUP
import com.travelsmartplus.models.responses.HttpResponses.INTERNAL_SERVER_ERROR
import com.travelsmartplus.models.responses.HttpResponses.NOT_FOUND
import com.travelsmartplus.utils.HashingService
import com.travelsmartplus.utils.Validator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines the general user routes to get, update and delete orgs. Add users can only be done by admin in [Route.adminRoutes]
 * @author Gabriel Salas
 */

fun Route.userRoutes() {
    val dao = UserDAOFacadeImpl()
    val hashingService = HashingService()

    route("/user/{id}") {
        // Get user
        get {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
                val user = dao.getUser(id) ?: throw NotFoundException()
                call.respond(HttpStatusCode.OK, user)
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
        post {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
                val editedUser = call.receive<User>()
                dao.editUser(id, editedUser)
                call.respond(HttpStatusCode.Created)
            } catch (e: BadRequestException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, FAILED_EDIT_USER)
            }
        }

        // Delete user
        delete {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw throw BadRequestException("Missing parameter")
                dao.deleteUser(id)
                call.respond(HttpStatusCode.OK)
            } catch (e: BadRequestException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, BAD_REQUEST)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, FAILED_DELETE_USER)
            }
        }
    }

    post("/user/{id}/setup") {
        try {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
            val request = call.receive<SetupAccountRequest>()
            Validator.validateSetupAccountRequest(request)

            // Check if user exists
            val user = dao.getUser(id) ?: throw NotFoundException()

            // Encrypt and store password
            val saltedHash = hashingService.generate(request.newPassword)
            dao.updatePassword(id, saltedHash)

            // Update user preferences and account setup
            user.preferredAirlines = request.preferredAirlines
            user.preferredHotelChains = request.preferredHotelChains
            user.accountSetup = true
            dao.editUser(id, user)

            call.respond(HttpStatusCode.Created)
        } catch (e: BadRequestException) {
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