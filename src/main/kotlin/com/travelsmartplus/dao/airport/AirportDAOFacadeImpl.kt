package com.travelsmartplus.dao.airport

import com.travelsmartplus.models.Airport
import com.travelsmartplus.models.AirportEntity
import com.travelsmartplus.models.Airports
import com.travelsmartplus.models.toAirport
import com.travelsmartplus.utils.DatabaseFactory.dbQuery

/**
 * Implementation of the [AirportDAOFacade] interface.
 * This class provides methods to get, add and delete airport information from the database.
 * @author Gabriel Salas
 */

class AirportDAOFacadeImpl : AirportDAOFacade {
    override suspend fun getAirport(id: Int): Airport? = dbQuery {
        AirportEntity.findById(id)?.toAirport()
    }

    override suspend fun getAirport(iata: String): Airport? = dbQuery {
        AirportEntity.find { Airports.iataCode eq iata }.singleOrNull()?.toAirport()

    }
}