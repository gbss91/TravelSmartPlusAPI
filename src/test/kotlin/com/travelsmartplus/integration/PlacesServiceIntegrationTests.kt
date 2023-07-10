package com.travelsmartplus.integration

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.dao.booking.BookingDAOFacadeImpl
import com.travelsmartplus.dao.hotel.HotelBookingDAOFacadeImpl
import com.travelsmartplus.services.PlacesServiceFacadeImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PlacesServiceIntegrationTests {

    private val placesService = PlacesServiceFacadeImpl()
    private val bookingDao = BookingDAOFacadeImpl()
    private val hotelBookingDao = HotelBookingDAOFacadeImpl()


    @Before
    fun setup() {
        DatabaseTestHelper.setup()
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `processBookingsImage should return updated bookings with image`() = runBlocking {

        val allBookings = bookingDao.getBookingsByUser(1)
        val updatedBookings = placesService.processBookingsImage(allBookings)

        assertEquals(true, updatedBookings[0].imageUrl?.isNotEmpty())
    }

    @Test
    fun `should return address`() = runBlocking {

        val hotelBooking = hotelBookingDao.getHotelBooking(1)
        val hotelBookings = if (hotelBooking != null) listOf(hotelBooking) else emptyList()
        val updatedBookings = placesService.getAddress(hotelBookings)

        assertEquals(true, updatedBookings[0].address.isNotEmpty())
    }


}