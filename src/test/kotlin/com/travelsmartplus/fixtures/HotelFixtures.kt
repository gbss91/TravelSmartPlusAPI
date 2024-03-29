package com.travelsmartplus.fixtures

import com.travelsmartplus.models.HotelBooking
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import java.math.BigDecimal
import kotlin.random.Random


object HotelFixtures {

    fun createMockHotelBooking(
        checkInDate: LocalDate,
        checkOutDate: LocalDate,
        index: Int
    ): HotelBooking {
        val hotelBooking = mockk<HotelBooking>()

        // Generate random rate between 50 and 300
        val rate = BigDecimal.valueOf(Random.nextDouble(50.0, 300.0))

        // Generate total price
        val durationOfStay = checkOutDate.dayOfYear - checkInDate.dayOfYear
        val totalPrice = rate * BigDecimal.valueOf(durationOfStay.toLong())

        // Generate random hotel chain code
        val hotelChains = listOf("HH", "HI", "MC")
        val randomChainCode = hotelChains[Random.nextInt(hotelChains.size)]

        // Configure properties of mock hotel booking
        every { hotelBooking.id } returns index
        every { hotelBooking.hotelName } returns "Test Hotel"
        every { hotelBooking.hotelChainCode } returns randomChainCode
        every { hotelBooking.address } returns "Address 123 St"
        every { hotelBooking.checkInDate } returns checkInDate
        every { hotelBooking.checkOutDate } returns checkOutDate
        every { hotelBooking.roomType } returns "Standard Room"
        every { hotelBooking.rate } returns rate
        every { hotelBooking.totalPrice } returns totalPrice
        every { hotelBooking.latitude } returns 40.714224
        every { hotelBooking.longitude } returns -73.961452

        return hotelBooking
    }
}