package com.travelsmartplus.services

import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.BookingSearchRequest

interface BookingServiceFacade {
    suspend fun newPredictedBooking(bookingSearchRequest: BookingSearchRequest, user: User): Booking?
    suspend fun addBooking(booking: Booking): Booking
}