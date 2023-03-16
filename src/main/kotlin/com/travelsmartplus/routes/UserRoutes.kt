package com.travelsmartplus.routes

import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.User
import com.travelsmartplus.utils.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {
    val dao = UserDAOFacadeImpl()
    val hashingService = HashingService()

    route("/user/{id}") {
        // Get user
        get {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                val user = dao.getUser(id) ?: throw NotFoundException()
                call.respond(HttpStatusCode.OK, user)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to get user: ${e.message}")
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
                call.respond(HttpStatusCode.InternalServerError, "Failed to update user: ${e.message}")
            }
        }

        // Delete user
        delete {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                dao.deleteUser(id)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to delete user: ${e.message}")
            }
        }
    }
}
