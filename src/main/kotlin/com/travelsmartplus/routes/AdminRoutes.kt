package com.travelsmartplus.routes

import com.travelsmartplus.dao.org.OrgDAOFacadeImpl
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.User
import com.travelsmartplus.utils.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminRoutes() {
    val userDAO = UserDAOFacadeImpl()
    val hashingService = HashingService()

    // Get all users
    get("/users/{orgId}") {
        try {
            val orgId = call.parameters["orgId"]?.toIntOrNull() ?: throw NotFoundException()
            val allUsers = userDAO.allUsers(orgId)
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
            if (userDAO.getUserByEmail(request.email) != null) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
                return@post
            }

            // Create hash and user
            val saltedHash = hashingService.generate(request.password)
            val user = User(
                orgId = request.orgId,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                admin = request.admin,
                password = saltedHash.hash,
                salt = saltedHash.salt
            )
            val isUserAdded = userDAO.addUser(user)
            if (isUserAdded == null) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to create user")
                return@post
            }
            call.respond(HttpStatusCode.Created, user.id!!)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create user: ${e.message}")
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                "Failed to create user"
            ) // No details for other exceptions to avoid disclosing private data
        }
    }


}