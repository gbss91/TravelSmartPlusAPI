package com.travelsmartplus.fixtures

import com.travelsmartplus.models.Airport

object AirportFixtures {
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
            airportName = "John F. Kennedy International Airport",
            city = "New York",
            country = "USA",
            iataCode = "JFK",
            icaoCode = "KJFK",
            latitude = 40.639722,
            longitude = -73.778889
        )

    )
}