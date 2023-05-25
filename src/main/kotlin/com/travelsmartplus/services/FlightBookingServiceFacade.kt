package com.travelsmartplus.services

import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.requests.BookingSearchRequest

interface FlightBookingServiceFacade {
    suspend fun getFlights(bookingSearchRequest: BookingSearchRequest): List<FlightBooking>
}