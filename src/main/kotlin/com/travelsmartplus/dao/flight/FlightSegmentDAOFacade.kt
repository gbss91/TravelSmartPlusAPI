package com.travelsmartplus.dao.flight

import com.travelsmartplus.models.FlightSegment

interface FlightSegmentDAOFacade {
    suspend fun getFlightSegment(id: Int): FlightSegment?
    suspend fun getFlightSegmentByBooking(bookingId: Int): List<FlightSegment>
    suspend fun addFlightSegment(flightSegment: FlightSegment, flightBookingId: Int): Int
    suspend fun deleteFlightSegment(id: Int)
}