package com.travelsmartplus.functional

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.fixtures.AirportFixtures
import com.travelsmartplus.fixtures.FlightFixtures.createMockFlightBooking
import com.travelsmartplus.fixtures.HotelFixtures.createMockHotelBooking
import com.travelsmartplus.fixtures.UserFixtures
import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.testModule
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BookingFunctionalTests {

    private lateinit var token: String
    private val bookingSearchRequest = BookingSearchRequest(
        userId = 1,
        oneWay= false,
        nonStop= true,
        origin= AirportFixtures.airports[0],
        destination= AirportFixtures.airports[1],
        departureDate= LocalDate(2023, 12, 5),
        returnDate= LocalDate(2023, 12, 10),
        adultsNumber= 1,
        travelClass= "ECONOMY",
        hotel= false
    )

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
        val request = client.post("api/bookingSearch") {
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
        val response = client.get("api/airports/all") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        val responseBody = response.bodyAsText()
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(responseBody)
    }

    @Test
    fun `get all airlines`() = testApplication {
        application { testModule() }
        val response = client.get("api/airlines/all") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        val responseBody = response.bodyAsText()
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(responseBody)
    }

    @Test
    fun `get all hotels`() = testApplication {
        application { testModule() }
        val response = client.get("api/hotels/all") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        val responseBody = response.bodyAsText()
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(responseBody)
    }

    @Test
    fun `fetch flight offers manually`() = testApplication {
        application { testModule() }
        val request = client.post("api/booking/flights") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(bookingSearchRequest))
        }

        val responseBody = request.bodyAsText()
        assertEquals(HttpStatusCode.OK, request.status)
        assertNotNull(responseBody)
    }

    @Test
    fun `fetch hotel offers manually`() = testApplication {
        application { testModule() }

        val hotelSearch = BookingSearchRequest(
            userId = 1,
            oneWay = false,
            nonStop = false,
            origin= AirportFixtures.airports[0],
            destination= AirportFixtures.airports[1],
            departureDate = LocalDate(2023, 11, 5),
            returnDate = LocalDate(2023, 11, 10),
            adultsNumber = 1,
            travelClass = "ECONOMY",
            hotel = true,
            checkInDate =  LocalDate(2023, 11, 5),
            checkOutDate = LocalDate(2023, 11, 10)
        )

        val request = client.post("api/booking/hotels") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(hotelSearch))
        }

        val responseBody = request.bodyAsText()
        assertEquals(HttpStatusCode.OK, request.status)
        assertNotNull(responseBody)
    }

    @Test
    fun `create a new booking`() = testApplication {
        application { testModule() }
        val flightBooking = createMockFlightBooking(
            AirportFixtures.airports[0],
            AirportFixtures.airports[1],
            LocalDate(2023, 12, 5).toJavaLocalDate(),
            LocalDate(2023, 12, 10).toJavaLocalDate(),
            6
        )
        val hotelBooking = createMockHotelBooking(
            LocalDate(2023, 12, 5),
            LocalDate(2023, 12, 10),
            6
        )

        val booking = Booking(
            user = UserFixtures.users[0],
            origin = AirportFixtures.airports[0],
            destination = AirportFixtures.airports[1],
            departureDate = LocalDate(2023, 12, 5),
            returnDate = LocalDate(2023, 12, 10),
            flightBooking = flightBooking,
            hotelBooking = hotelBooking,
            adultsNumber = 1,
            status = "CONFIRMED",
            totalPrice = 360.toBigDecimal()
        )

        val request = client.post("api/booking") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(booking))
        }

        val responseBody = request.bodyAsText()
        assertEquals(HttpStatusCode.OK, request.status)
        assertNotNull(responseBody)
    }

    @Test
    fun `get all user bookings`() = testApplication {
        application { testModule() }
        val response = client.get("api/bookings/1") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        val responseBody = response.bodyAsText()
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(responseBody)
    }

    @Test
    fun `get specific booking`() = testApplication {
        application { testModule() }
        val response = client.get("api/booking/1") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        val responseBody = response.bodyAsText()
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(responseBody)
    }

    @Test
    fun `delete a booking`() = testApplication {
        application { testModule() }
        val response = client.delete("api/booking/1") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        val responseBody = response.bodyAsText()
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(responseBody)
    }
}