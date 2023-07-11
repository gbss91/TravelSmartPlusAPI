package com.travelsmartplus

import com.travelsmartplus.plugins.configureHTTP
import com.travelsmartplus.plugins.configureRouting
import com.travelsmartplus.plugins.configureSecurity
import com.travelsmartplus.plugins.configureSerialization
import com.travelsmartplus.utils.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val host = System.getenv("HOST")
val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

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


