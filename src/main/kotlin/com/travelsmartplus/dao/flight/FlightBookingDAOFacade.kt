package com.travelsmartplus.dao.flight

import com.travelsmartplus.models.FlightBooking

interface FlightBookingDAOFacade {
    suspend fun getFlightBooking(id: Int): FlightBooking?
    suspend fun addFlightBooking(flightBooking: FlightBooking): Int
    suspend fun deleteFlightBooking(id: Int)
}