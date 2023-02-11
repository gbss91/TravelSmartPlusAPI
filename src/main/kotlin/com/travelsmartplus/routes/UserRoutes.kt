package com.travelsmartplus.routes

import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {

    val dao = UserDAOFacadeImpl()

    //Get all users
    get("/users/{orgId}") {
        val orgId = call.parameters["orgId"]?.toIntOrNull() ?: throw NotFoundException()
        val allUsers = dao.allUsers(orgId)
        call.respond(allUsers)
    }

    //Create user
    post("/user") {
        val newUser = call.receive<User>()
        dao.addUser(newUser)
        call.respond(HttpStatusCode.Accepted)
    }

    route("user/{id}") {

        //Get user
        get() {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
            val user = dao.getUser(id)
            if(user != null) {
                call.respond(user)
            }
        }

        //Update user
        post {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
            val user = call.receive<User>()
            dao.editUser(id, user.firstName, user.lastName, user.email, user.password, user.salt)
            call.respond(HttpStatusCode.Accepted)
        }

        //Delete user
        delete {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
            dao.deleteUser(id)
            call.respond(HttpStatusCode.OK)
        }
    }

}