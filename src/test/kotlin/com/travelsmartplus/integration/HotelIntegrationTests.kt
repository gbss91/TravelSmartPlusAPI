package com.travelsmartplus.integration

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.dao.hotel.HotelBookingFacadeImpl
import com.travelsmartplus.models.HotelBooking
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class HotelIntegrationTests {

    private val dao = HotelBookingFacadeImpl()

    @Before
    fun setup() {
        DatabaseTestHelper.setup()
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `get hotel booking with id`() = runBlocking {
        val hotelBooking = dao.getHotelBooking(1)
        assertEquals(1, hotelBooking?.id)
    }

    @Test
    fun `add hotel booking`() = runBlocking {
        val newHotelBooking = HotelBooking(
            hotelName = "Crowne Plaza Dublin",
            address = "Northwood Avenue, Dublin, Ireland",
            checkInDate = LocalDate(2023, 11, 1),
            checkOutDate = LocalDate(2023, 11, 5),
            rate = BigDecimal("120.00"),
            totalPrice = BigDecimal("480.00"),
            latitude = 37.7749,
            longitude = -122.4194
        )

        val hotel = dao.addHotelBooking(newHotelBooking)
        assertEquals(2, hotel)
    }

    @Test
    fun `delete hotel booking`() = runBlocking {
        dao.deleteHotelBooking(1)
        val deletedHotelBooking = dao.getHotelBooking(1)
        assertEquals(null, deletedHotelBooking)
    }
}