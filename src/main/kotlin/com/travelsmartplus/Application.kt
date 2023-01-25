package com.travelsmartplus

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.travelsmartplus.plugins.*
import com.travelsmartplus.dao.*

fun main() {
    embeddedServer(Netty, port = 8080, watchPaths = listOf("classes"), host = "127.0.0.1", module = Application::module)
            .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
}
