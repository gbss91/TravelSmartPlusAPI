package com.travelsmartplus.functional

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.testModule
import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class BookingFunctionalTests {

    private lateinit var token: String

    @Before
    fun setup() {
        DatabaseTestHelper.setup()
        token = DatabaseTestHelper.signIn(email = "john@test.com", password = "myPass123")
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `search booking`() = testApplication {
        application { testModule() }

        val request = client.post("api/user/{id}/bookingSearch")



    }

    @Test
    fun `get airports with query`() = testApplication {
        application { testModule() }




    }
}