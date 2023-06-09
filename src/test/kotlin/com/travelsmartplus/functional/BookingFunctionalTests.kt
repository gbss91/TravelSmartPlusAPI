package com.travelsmartplus.functional

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.fixtures.AirportFixtures
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.testModule
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
    fun `search booking flight only`() = testApplication {
        application { testModule() }

        val bookingSearchRequest = BookingSearchRequest(
            oneWay= false,
            nonStop= true,
            origin= AirportFixtures.airports[0],
            destination= AirportFixtures.airports[1],
            departureDate= LocalDate(2023, 11, 5),
            returnDate= LocalDate(2023, 11, 10),
            adultsNumber= 1,
            travelClass= "ECONOMY",
            hotel= false
        )

        val request = client.post("api/user/1/bookingSearch") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(bookingSearchRequest))
        }

        val responseBody = request.bodyAsText()
        assertEquals(HttpStatusCode.OK, request.status)
        assertNotNull(responseBody)
    }

    @Test
    fun `get all airports`() = testApplication {
        application { testModule() }
        val response = client.get("api/airports/search") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        val responseBody = response.bodyAsText()
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(responseBody)
    }
}