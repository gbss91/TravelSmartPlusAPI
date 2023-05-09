package com.travelsmartplus.dao.flight

import com.travelsmartplus.models.Flight

interface FlightDAOFacade {
    suspend fun getFlight(id: Int): Flight?
    suspend fun getFlightsBySegment(segmentId: Int): List<Flight>
    suspend fun addFlight(flight: Flight, flightSegmentId: Int): Flight
    suspend fun deleteFlight(id: Int)
}