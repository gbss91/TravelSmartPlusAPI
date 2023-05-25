package com.travelsmartplus.integration

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.dao.airport.AirportDAOFacadeImpl
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.services.FlightBookingServiceFacadeImpl
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class FlightBookingServiceIntegrationTests {

    private val flightService: FlightBookingServiceFacadeImpl = FlightBookingServiceFacadeImpl()
    private val airportDao = AirportDAOFacadeImpl()

    @Before
    fun setup() {
        DatabaseTestHelper.setup()
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `getFlights should return a list of flight bookings`() = runBlocking {
        val bookingSearchRequest = BookingSearchRequest(
            oneWay = false,
            nonStop = true,
            origin = airportDao.getAirport("DUB")!!,
            destination = airportDao.getAirport("LAX")!!,
            departureDate = LocalDate(2023, 11, 5),
            returnDate = LocalDate(2023, 11, 10),
            adultsNumber = 1,
            travelClass = "ECONOMY",
            checkInDate =  LocalDate(2023, 11, 5),
            checkOutDate = LocalDate(2023, 11, 10)
        )

        val flightBookings = flightService.getFlights(bookingSearchRequest)
        assertEquals(true, flightBookings.isNotEmpty())
        println(flightBookings)
    }

    @Test
    fun `getFlights should return empty list when exception`() = runBlocking {
        val bookingSearchRequest = BookingSearchRequest(
            oneWay = false,
            nonStop = true,
            origin = airportDao.getAirport("DUB")!!,
            destination = airportDao.getAirport("LAX")!!,
            departureDate = LocalDate(2023, 11, 5),
            returnDate = LocalDate(2023, 11, 10),
            adultsNumber = 1,
            travelClass = "WRONG CLASS", // Wrong class - Throw error
            checkInDate =  LocalDate(2023, 11, 5),
            checkOutDate = LocalDate(2023, 11, 10)
        )
        val flightBookings = flightService.getFlights(bookingSearchRequest)
        assertEquals(true, flightBookings.isEmpty())

    }
}