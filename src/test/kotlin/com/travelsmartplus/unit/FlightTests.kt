package com.travelsmartplus.unit

import com.travelsmartplus.models.Airport
import com.travelsmartplus.models.Flight
import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.FlightSegment
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.time.Duration

class FlightTests {

    @Test
    fun `create flight booking object`() {
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

        val flightSegment = FlightSegment(
            id = 1,
            flights = listOf(
                Flight(
                    id = 1,
                    departureAirport = origin,
                    departureTime = LocalDateTime(2023, 6, 5, 18, 50),
                    arrivalAirport = destination,
                    arrivalTime = LocalDateTime(2023, 6, 6, 5, 7),
                    carrierIataCode = "AA",
                    carrierName = ""
                )
            ),
            direction = "outbound",
            duration = Duration.parse("PT11H17M"),
            stops = 0
        )

        val flightBooking = FlightBooking(
            id = 1,
            bookingReference = "AB123",
            oneWay = true,
            originCity = "Dublin",
            destinationCity = "Los Angeles",
            segments = listOf(flightSegment),
            travelClass = "Economy",
            status = "Confirmed",
            totalPrice = BigDecimal("599.99")
        )

        // Test Serializer
        val json = Json.encodeToString(flightBooking)
        val deserialized = Json.decodeFromString<FlightBooking>(json)

        assertEquals(flightBooking, deserialized)
    }

}