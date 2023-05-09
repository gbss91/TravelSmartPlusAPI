package com.travelsmartplus.fixtures

import com.travelsmartplus.models.Airport
import com.travelsmartplus.models.Flight
import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.FlightSegment
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal
import kotlin.time.Duration

object FlightFixtures {
    val airports = listOf(
        Airport(
            id = 1,
            airportName = "Dublin Airport",
            city = "Dublin",
            country = "Ireland",
            iataCode = "DUB",
            icaoCode = "EIDW",
            latitude = 53.421299,
            longitude = -6.27007
        ),
        Airport(
            id = 2,
            airportName = "Los Angeles International Airport",
            city = "Los Angeles",
            country = "USA",
            iataCode = "LAX",
            icaoCode = "KLAX",
            latitude = 33.94250107,
            longitude = -118.4079971
        )
    )

    val flights = listOf(
        Flight(
            id = 1,
            departureAirport = airports[0],
            departureTime = LocalDateTime(2023, 6, 5, 18, 50),
            arrivalAirport = airports[1],
            arrivalTime = LocalDateTime(2023, 6, 6, 5, 7),
            carrierIataCode = "AA"
        ),
        Flight(
            id = 2,
            departureAirport = airports[1],
            departureTime = LocalDateTime(2023, 6, 5, 18, 50),
            arrivalAirport = airports[0],
            arrivalTime = LocalDateTime(2023, 6, 6, 5, 7),
            carrierIataCode = "AA"
        ),
    )

    val flightSegments = listOf(
        FlightSegment(
                flights = listOf(flights[0]),
                direction = "outbound",
                duration = Duration.parse("PT11H17M"),
                stops = 0
        )
    )

    val flightBookings = listOf(
        FlightBooking(
            id = 1,
            bookingReference = "AB123",
            oneWay = true,
            originCity = "Dublin",
            destinationCity = "Los Angeles",
            segments = listOf(flightSegments[0]),
            travelClass = "Economy",
            status = "Confirmed",
            totalPrice = BigDecimal("599.99")
        )
    )



}