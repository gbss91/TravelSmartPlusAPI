package com.travelsmartplus.routes

import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.User
import com.travelsmartplus.utils.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {
    val dao = UserDAOFacadeImpl()
    val hashingService = HashingService()

    // Get all users
    get("/users/{orgId}") {
        try {
            val orgId = call.parameters["orgId"]?.toIntOrNull() ?: throw NotFoundException()
            val allUsers = dao.allUsers(orgId)
            call.respond(HttpStatusCode.OK, allUsers)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to get user: ${e.message}")
        }
    }

    // Create user
    post("/user") {
        try {
            val request = call.receive<User>()

            // Validate duplicate user
            if (dao.getUserByEmail(request.email) != null) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
                return@post
            }

            // Create hash and user
            val saltedHash = hashingService.generate(request.password)
            val user = User(orgId = request.orgId, firstName = request.firstName, lastName = request.lastName, email = request.email, password = saltedHash.hash, salt = saltedHash.salt)
            val isUserAdded = dao.addUser(user)
            if (isUserAdded == null) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to create user")
                return@post
            }
            call.respond(HttpStatusCode.Created, user.id!!)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create user: ${e.message}")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create user") // No details for other exceptions to avoid disclosing private data
        }
    }

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
                dao.editUser(id, user.firstName, user.lastName, user.email, user.password, user.salt)
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
