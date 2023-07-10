package com.travelsmartplus.plugins

import com.travelsmartplus.routes.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {

            // Admin-only routes
            authenticate("adminAuth-jwt") {
                adminRoutes()
                orgRoutes()
            }

            authenticate("auth-jwt") {
                userRoutes()
                bookingRoutes()
            }

            authRoutes()
        }
    }
}
