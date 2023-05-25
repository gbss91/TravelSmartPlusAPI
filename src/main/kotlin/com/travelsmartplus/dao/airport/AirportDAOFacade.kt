package com.travelsmartplus.dao.airport

import com.travelsmartplus.models.Airport

/**
 * Gets airport information from database - Only read
 * Data will be imported using [OpenFlights datasets](https://openflights.org/data.html)
 * @author Gabriel Salas
 */

interface AirportDAOFacade {
    suspend fun getAirport(id: Int): Airport?
    suspend fun getAirport(iata: String): Airport?
}