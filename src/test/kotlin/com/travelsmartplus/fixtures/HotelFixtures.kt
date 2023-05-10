package com.travelsmartplus.fixtures

import com.travelsmartplus.models.HotelBooking
import kotlinx.datetime.LocalDate
import java.math.BigDecimal

object HotelFixtures {
    val hotelBookings = listOf(
        HotelBooking(
            id = 1,
            hotelName = "Radisson Hotel",
            address = "123 Main St, Los Angeles, USA",
            checkInDate = LocalDate(2023, 6, 1),
            checkOutDate = LocalDate(2023, 6, 5),
            rate = BigDecimal("99.99"),
            totalPrice = BigDecimal("399.96"),
            latitude = 37.7749,
            longitude = -122.4194
        )
    )
}