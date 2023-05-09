package com.travelsmartplus.unit

import com.travelsmartplus.models.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Test
import java.math.BigDecimal
import kotlin.time.Duration

class BookingTests {

    @Test
    fun `create booking with valid values`() {
        val user = User(
            id = 1,
            orgId = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            admin = false,
            password = "password",
            salt = "salt"
        )

        val origin = Airport(
            id = 1,
            airportName = "Dublin Airport",
            city = "Dublin",
            country = "Ireland",
            iataCode = "DUB",
            icaoCode = "EIDW",
            latitude = 53.421299,
            longitude = -6.27007
        )

        val destination = Airport(
            id = 2,
            airportName = "Los Angeles International Airport",
            city = "Los Angeles",
            country = "USA",
            iataCode = "LAX",
            icaoCode = "KLAX",
            latitude = 33.94250107,
            longitude = -118.4079971
        )

        val flight1 = Flight(
            id = 1,
            departureAirport = origin,
            departureTime = LocalDateTime.parse("2023-06-05T18:50"),
            arrivalAirport = destination,
            arrivalTime = LocalDateTime.parse("2023-06-06T09:07"),
            carrierIataCode = "UA"
        )

        val flight2 = Flight(
            id = 2,
            departureAirport = destination,
            departureTime = LocalDateTime.parse("2023-06-15T14:30"),
            arrivalAirport = origin,
            arrivalTime = LocalDateTime.parse("2023-06-16T09:15"),
            carrierIataCode = "UA"
        )

        val flightSegment1 = FlightSegment(
            id = 1,
            flights = listOf(flight1),
            direction = "Outbound",
            duration = Duration.parse("PT11H17M"),
            stops = 0
        )

        val flightSegment2 = FlightSegment(
            id = 2,
            flights = listOf(flight2),
            direction = "Inbound",
            duration = Duration.parse("PT10H45M"),
            stops = 0
        )

        val flightBooking = FlightBooking(
            id = 1,
            bookingReference = "ABCD1234",
            oneWay = false,
            originCity = "Dublin",
            destinationCity = "Los Angeles",
            segments = listOf(flightSegment1, flightSegment2),
            travelClass = "Economy",
            status = "Booked",
            totalPrice = BigDecimal("760.00")
        )

        val booking = Booking(
            id = 1,
            user = user,
            origin = origin,
            destination = destination,
            departureDate = LocalDate(2023, 6, 5),
            returnDate = LocalDate(2023, 6, 15),
            flightBooking = flightBooking,
            adultsNumber = 2,
            status = "CONFIRMED",
            totalPrice = BigDecimal("760.00")
        )

        assert(booking.id == 1)
        assert(booking.user == user)
        assert(booking.origin == origin)
        assert(booking.destination == destination)
        assert(booking.departureDate == LocalDate(2023, 6, 5))
        assert(booking.returnDate == LocalDate(2023, 6, 15))
        assert(booking.flightBooking == flightBooking)
        assert(booking.adultsNumber == 2)
        assert(booking.status == "CONFIRMED")
        assert(booking.totalPrice == BigDecimal("760.00"))
    }
}