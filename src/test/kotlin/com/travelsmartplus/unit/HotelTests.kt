package com.travelsmartplus.unit

import com.travelsmartplus.models.HotelBooking
import junit.framework.TestCase.assertEquals
import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import java.math.BigDecimal

class HotelTests {

    @Test
    fun `create new hotel booking`() {
        val booking = HotelBooking(
            hotelName = "Radisson Hotel",
            address = "123 Main St, Los Angeles, USA",
            checkInDate = LocalDate(2023, 6, 1),
            checkOutDate = LocalDate(2023, 6, 5),
            rate = BigDecimal("99.99"),
            totalPrice = BigDecimal("399.96"),
            latitude = 37.7749,
            longitude = -122.4194
        )

        // Test Serialization
        val json = Json.encodeToString(booking)
        val deserialized = Json.decodeFromString<HotelBooking>(json)

        assertEquals(booking, deserialized)
    }
}