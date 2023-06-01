package com.travelsmartplus.fixtures

import com.travelsmartplus.models.Airport
import com.travelsmartplus.models.Flight
import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.FlightSegment
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.toKotlinLocalDateTime
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.random.Random
import kotlin.time.Duration.Companion.hours

object FlightFixtures {

    fun createMockFlightBooking(
        origin: Airport,
        destination: Airport,
        departureDate: LocalDate,
        returnDate: LocalDate
    ): FlightBooking {
        val flightBooking = mockk<FlightBooking>()

        // Generate random travel class
        val travelClasses = listOf("ECONOMY", "PREMIUM_ECONOMY", "BUSINESS", "FIRST")
        val randomClass = travelClasses[Random.nextInt(travelClasses.size)]

        // Create flight segments
        val flightSegments = listOf(
            createMockFlightSegment(origin, destination, departureDate, "OUTBOUND"),
            createMockFlightSegment(destination, origin, returnDate, "INBOUND")
        )

        // Generate random totalPrice
        val totalPrice = BigDecimal.valueOf(Random.nextDouble(500.0, 1200.0))

        every { flightBooking.bookingReference } returns "AX9G75"
        every { flightBooking.oneWay } returns false
        every { flightBooking.originCity } returns origin.city
        every { flightBooking.destinationCity } returns destination.city
        every { flightBooking.segments } returns flightSegments
        every { flightBooking.travelClass } returns randomClass
        every { flightBooking.status } returns "CONFIRMED"
        every { flightBooking.totalPrice } returns totalPrice

        return flightBooking
    }

    private fun createMockFlightSegment(
        origin: Airport,
        destination: Airport,
        departure: LocalDate,
        direction: String
    ): FlightSegment {
        val flightSegment = mockk<FlightSegment>()
        val flights = listOf(
            createMockFlight(origin, destination, departure)
        )

        every { flightSegment.flights } returns flights
        every { flightSegment.direction } returns direction
        every { flightSegment.duration } returns 7.hours
        every { flightSegment.stops } returns 0

        return flightSegment
    }

    private fun createMockFlight(
        origin: Airport,
        destination: Airport,
        departure: LocalDate
    ): Flight {
        val flight = mockk<Flight>()

        // Generate random departure time
        val hour = Random.nextInt(24)
        val minute = Random.nextInt(60)
        val second = Random.nextInt(60)
        val departureTime = departure.atTime(hour, minute, second)

        // Calculate arrival time 7 hours later (approx flight time DUB-NYC)
        val arrivalTime = departureTime.plusHours(7)

        // Generate random airline IATA code
        val iataCodes = listOf("UA", "DL", "EI", "AF", "LH")
        val randomAirlineCode = iataCodes[Random.nextInt(iataCodes.size)]

        every { flight.departureAirport } returns origin
        every { flight.departureTime } returns departureTime.toKotlinLocalDateTime()
        every { flight.arrivalAirport } returns destination
        every { flight.arrivalTime } returns arrivalTime.toKotlinLocalDateTime()
        every { flight.carrierIataCode } returns randomAirlineCode

        return flight
    }
}

