package com.travelsmartplus

import com.travelsmartplus.plugins.configureHTTP
import com.travelsmartplus.plugins.configureRouting
import com.travelsmartplus.plugins.configureSecurity
import com.travelsmartplus.plugins.configureSerialization
import com.travelsmartplus.utils.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

// Use ENV variable for production - Else localhost for development
val host: String = System.getenv("HOST") ?: "127.0.0.1"
val port = System.getenv("PORT")?.toIntOrNull() ?: 8000

fun main() {
    embeddedServer(Netty, port = port, watchPaths = listOf("classes"), host = host, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
}

// Modules for testing
fun Application.testModule() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
}


