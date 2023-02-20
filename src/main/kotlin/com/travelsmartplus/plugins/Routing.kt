package com.travelsmartplus.plugins

import com.travelsmartplus.routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            userRoutes()
            orgRoutes()
            authRoutes()
        }
    }
}
