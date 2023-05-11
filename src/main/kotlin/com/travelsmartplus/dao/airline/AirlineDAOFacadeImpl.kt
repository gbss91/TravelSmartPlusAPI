package com.travelsmartplus.dao.airline

import com.travelsmartplus.models.Airline
import com.travelsmartplus.models.AirlineEntity
import com.travelsmartplus.models.Airlines
import com.travelsmartplus.models.toAirline
import com.travelsmartplus.utils.DatabaseFactory.dbQuery

/**
 * Implementation of the [AirlineDAOFacade] interface.
 * This class provides methods to get, add and delete airline information from the database.
 * @author Gabriel Salas
 */

class AirlineDAOFacadeImpl : AirlineDAOFacade {
    override suspend fun getAirline(id: Int): Airline? = dbQuery {
        AirlineEntity.findById(id)?.toAirline()
    }

    override suspend fun getAirline(iata: String): Airline? = dbQuery {
        AirlineEntity.find { Airlines.iataCode eq iata }.singleOrNull()?.toAirline()
    }
}