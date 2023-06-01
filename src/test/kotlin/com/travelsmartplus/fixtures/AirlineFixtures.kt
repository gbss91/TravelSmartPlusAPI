package com.travelsmartplus.fixtures

import com.travelsmartplus.models.Airline

object AirlineFixtures {
    val airlines = listOf(
        Airline(
            id = 1,
            airlineName = "United Airlines",
            iataCode = "UA",
            icaoCode = "UAL"
        ),
        Airline(
            id = 2,
            airlineName = "Aer Lingus",
            iataCode = "EI",
            icaoCode = "EIN"
        ),
        Airline(
            id = 3,
            airlineName = "American Airlines",
            iataCode = "AA",
            icaoCode = "AAL"
        ),
        Airline(
            id = 4,
            airlineName = "Delta Airlines",
            iataCode = "DL",
            icaoCode = "DAL"
        )
    )
}