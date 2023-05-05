package com.travelsmartplus.dao.airline

import com.travelsmartplus.models.Airline

interface AirlineDAOFacade {
    suspend fun getAirline(id: Int): Airline?
    suspend fun getAirline(iata: String): Airline?
}