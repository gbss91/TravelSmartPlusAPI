package com.travelsmartplus.routes

import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.User
import com.travelsmartplus.models.responses.HttpResponses.FAILED_DELETE_USER
import com.travelsmartplus.models.responses.HttpResponses.FAILED_EDIT_USER
import com.travelsmartplus.models.responses.HttpResponses.INTERNAL_SERVER_ERROR
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
    // val hashingService = HashingService()

    route("/user/{id}") {
        // Get user
        get {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                val user = dao.getUser(id) ?: throw NotFoundException()
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, INTERNAL_SERVER_ERROR)
            }
        }

        // Update user
        post {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                val user = call.receive<User>()
                dao.editUser(id, user.firstName, user.lastName, user.email, user.admin, user.password, user.salt)
                call.respond(HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, FAILED_EDIT_USER)
            }
        }

        // Delete user
        delete {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                dao.deleteUser(id)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, FAILED_DELETE_USER)
            }
        }
    }
}
