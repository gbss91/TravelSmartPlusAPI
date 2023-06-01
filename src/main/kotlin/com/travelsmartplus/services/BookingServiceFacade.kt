package com.travelsmartplus.services

import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.BookingSearchRequest

interface BookingServiceFacade {
    suspend fun new(bookingSearchRequest: BookingSearchRequest, previousBookings: List<Booking>, user: User): Booking?
}