package com.travelsmartplus

<<<<<<< HEAD
import com.travelsmartplus.dao.*
import com.travelsmartplus.plugins.*
=======
import com.travelsmartplus.plugins.configureHTTP
import com.travelsmartplus.plugins.configureRouting
import com.travelsmartplus.plugins.configureSecurity
import com.travelsmartplus.plugins.configureSerialization
import com.travelsmartplus.utils.DatabaseFactory
>>>>>>> development
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8000, watchPaths = listOf("classes"), host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
}
<<<<<<< HEAD
=======

// Modules for testing
fun Application.testModule() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()
}


>>>>>>> development
