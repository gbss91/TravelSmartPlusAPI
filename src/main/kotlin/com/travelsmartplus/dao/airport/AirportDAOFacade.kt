package com.travelsmartplus.dao.airport

import com.travelsmartplus.models.Airport

// Data will be imported using OpenFlights datasets [https://openflights.org/data.html]
// Only functions retrieve data required
interface AirportDAOFacade {
    suspend fun getAirport(id: Int): Airport?
    suspend fun getAirport(iata: String): Airport?
}