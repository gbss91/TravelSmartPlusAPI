package com.travelsmartplus.fixtures

import com.travelsmartplus.models.Booking
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.toKotlinLocalDate
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.random.Random

object BookingFixtures {

    private val admin = UserFixtures.users[0]
    private val origin = AirportFixtures.airports[0]
    private val destination = AirportFixtures.airports[1]

    fun createMockBookings(): List<Booking> {
        val bookings = mutableListOf<Booking>()


        for (i in 1..6) {
            val booking = mockk<Booking>()

            // Generate random total price between 700 and 1500
            val totalPrice = BigDecimal.valueOf(Random.nextDouble(700.0, 1500.0))

            // Generate random dates
            val departureDate = generateRandomDate()
            val returnDate = departureDate.plusDays((0..10).random().toLong())


            every { booking.id } returns (i+1)
            every { booking.user } returns admin
            every { booking.origin } returns origin
            every { booking.destination } returns destination
            every { booking.departureDate } returns departureDate.toKotlinLocalDate()
            every { booking.returnDate } returns returnDate.toKotlinLocalDate()
            every { booking.flightBooking } returns FlightFixtures.createMockFlightBooking(origin, destination, departureDate, returnDate)
            every { booking.hotelBooking } returns HotelFixtures.createMockHotelBooking(departureDate.toKotlinLocalDate(), returnDate.toKotlinLocalDate())
            every { booking.adultsNumber } returns 1
            every { booking.status } returns "CONFIRMED"
            every { booking.totalPrice } returns totalPrice

            bookings.add(booking)
        }

        return bookings
    }

    private fun generateRandomDate(): LocalDate {
        val currentDate = LocalDate.now()
        val maxDaysAhead = 365 // Maximum number of days ahead in the future

        val randomDays = (0..maxDaysAhead).random().toLong()
        return currentDate.plusDays(randomDays)
    }

}