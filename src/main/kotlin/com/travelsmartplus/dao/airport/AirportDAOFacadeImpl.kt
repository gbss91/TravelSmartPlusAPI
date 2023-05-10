package com.travelsmartplus.dao.airport

import com.travelsmartplus.models.Airport
import com.travelsmartplus.models.AirportEntity
import com.travelsmartplus.models.Airports
import com.travelsmartplus.models.toAirport
import com.travelsmartplus.utils.DatabaseFactory.dbQuery

class AirportDAOFacadeImpl : AirportDAOFacade {
    override suspend fun getAirport(id: Int): Airport? = dbQuery {
        AirportEntity.findById(id)?.toAirport()
    }

    override suspend fun getAirport(iata: String): Airport? = dbQuery {
        AirportEntity.find { Airports.iataCode eq iata }.singleOrNull()?.toAirport()

    }
}