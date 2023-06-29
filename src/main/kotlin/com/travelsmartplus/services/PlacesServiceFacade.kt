package com.travelsmartplus.services

import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.requests.BookingSearchRequest

interface PlacesServiceFacade {
    suspend fun processBookingsImage(allBookings: List<Booking>): List<Booking>
}