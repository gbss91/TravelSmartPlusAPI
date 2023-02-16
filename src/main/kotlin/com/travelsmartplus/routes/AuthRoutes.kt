package com.travelsmartplus.routes

import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.User
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.authRoutes() {

    val dao = UserDAOFacadeImpl()

    get("/signin") {
        val user = call.receive<User>()


    }

    get("/signup") {

    }

}