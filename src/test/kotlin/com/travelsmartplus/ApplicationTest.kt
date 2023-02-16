package com.travelsmartplus

import kotlin.test.*
import io.ktor.server.testing.*
import com.travelsmartplus.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
    }
}