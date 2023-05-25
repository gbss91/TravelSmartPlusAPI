package com.travelsmartplus.integration

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.dao.airport.AirportDAOFacadeImpl
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.services.HotelBookingServiceFacadeImpl
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class HotelBookingServiceIntegrationTests {

    private val hotelService: HotelBookingServiceFacadeImpl = HotelBookingServiceFacadeImpl()
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
    fun `getHotels should return list of hotel bookings`() = runBlocking {
        val bookingSearchRequest = BookingSearchRequest(
            oneWay = false,
            nonStop = false,
            origin = airportDao.getAirport("DUB")!!,
            destination = airportDao.getAirport("LAX")!!,
            departureDate = LocalDate(2023, 6, 5),
            returnDate = LocalDate(2023, 6, 10),
            adultsNumber = 1,
            travelClass = "ECONOMY",
            checkInDate =  LocalDate(2023, 6, 5),
            checkOutDate = LocalDate(2023, 6, 10)
        )


        val hotelBookings = hotelService.getHotels(bookingSearchRequest)
        assertEquals(true, hotelBookings.isNotEmpty())
    }

    @Test
    fun `getHotels should return empty list when exception`() = runBlocking {
        val bookingSearchRequest = BookingSearchRequest(
            oneWay = false,
            nonStop = false,
            origin = airportDao.getAirport("DUB")!!,
            destination = airportDao.getAirport("LAX")!!,
            departureDate = LocalDate(2023, 11, 5),
            returnDate = LocalDate(2023, 11, 10),
            adultsNumber = 1,
            travelClass = "ECONOMY",
            checkInDate =  LocalDate(2023, 11, 5),
            checkOutDate = LocalDate(2023, 11, 3) // Wrong date
        )


        val hotelBookings = hotelService.getHotels(bookingSearchRequest)
        assertEquals(true, hotelBookings.isEmpty())
    }

}