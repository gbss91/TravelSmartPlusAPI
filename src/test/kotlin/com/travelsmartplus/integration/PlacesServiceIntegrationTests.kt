package com.travelsmartplus.integration

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.dao.booking.BookingDAOFacadeImpl
import com.travelsmartplus.services.PlacesServiceFacadeImpl
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PlacesServiceIntegrationTests {

    private val placesService = PlacesServiceFacadeImpl()
    private val bookingDao = BookingDAOFacadeImpl()


    @Before
    fun setup() {
        DatabaseTestHelper.setup()
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `processBookingsImage should return updated bookings`() = runBlocking {

        val allBookings = bookingDao.getBookingsByUser(1)
        val updatedBookings = placesService.processBookingsImage(allBookings)

        assertEquals(true, updatedBookings[0].imageUrl?.isNotEmpty())
    }
}