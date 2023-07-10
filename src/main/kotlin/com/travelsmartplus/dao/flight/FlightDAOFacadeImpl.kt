package com.travelsmartplus.dao.flight

import com.travelsmartplus.models.*
import com.travelsmartplus.utils.DatabaseFactory.dbQuery
import io.ktor.server.plugins.*
import kotlinx.datetime.toJavaLocalDateTime

/**
 * Implementation of the [FlightDAOFacade] interface.
 * This class provides methods to get, add and delete flight information from the database.
 * @author Gabriel Salas
 */

class FlightDAOFacadeImpl : FlightDAOFacade {
    override suspend fun getFlight(id: Int): Flight? = dbQuery {
        FlightEntity.findById(id)?.toFlight()
    }

    override suspend fun getFlightsBySegment(segmentId: Int): List<Flight> = dbQuery {
        FlightEntity.find { Flights.flightSegmentId eq segmentId }.map { it.toFlight() }
    }

    override suspend fun addFlight(flight: Flight, flightSegmentId: Int): Flight = dbQuery {
        val flightSegment = FlightSegmentEntity[flightSegmentId]
        val departureAirport = AirportEntity.find { Airports.iataCode eq flight.departureAirport.iataCode }.single()
        val arrivalAirport = AirportEntity.find { Airports.iataCode eq flight.arrivalAirport.iataCode }.single()
        FlightEntity.new {
            this.flightSegmentId = flightSegment
            departureIataCode = departureAirport.iataCode
            departureTime = flight.departureTime.toJavaLocalDateTime()
            arrivalIataCode = arrivalAirport.iataCode
            arrivalTime = flight.arrivalTime.toJavaLocalDateTime()
            carrierIataCode = flight.carrierIataCode
        }.toFlight()
    }

    override suspend fun deleteFlight(id: Int) = dbQuery {
        val flight = FlightEntity.findById(id) ?: throw NotFoundException("Flight not found")
        flight.delete()
    }
}