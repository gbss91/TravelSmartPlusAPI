package com.travelsmartplus.plugins

import com.travelsmartplus.routes.*
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            userRoutes()
        }
    }
}
