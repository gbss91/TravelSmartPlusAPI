package com.travelsmartplus.dao.airline

import com.travelsmartplus.models.Airline
import com.travelsmartplus.models.AirlineEntity
import com.travelsmartplus.models.Airlines
import com.travelsmartplus.models.toAirline
import com.travelsmartplus.utils.DatabaseFactory.dbQuery

class AirlineDAOFacadeImpl : AirlineDAOFacade {
    override suspend fun getAirline(id: Int): Airline? = dbQuery {
        AirlineEntity.findById(id)?.toAirline()
    }

    override suspend fun getAirline(iata: String): Airline? = dbQuery {
        AirlineEntity.find { Airlines.iataCode eq iata }.singleOrNull()?.toAirline()
    }
}