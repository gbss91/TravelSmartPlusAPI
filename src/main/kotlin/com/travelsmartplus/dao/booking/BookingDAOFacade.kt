package com.travelsmartplus.dao.booking

import com.travelsmartplus.models.Booking

interface BookingDAOFacade {
    suspend fun getBooking(id: Int): Booking?
    suspend fun getBookingsByUser(userId: Int): List<Booking>
    suspend fun getAllBookings(orgId: Int): List<Booking>
    suspend fun addBooking(booking: Booking): Booking
    suspend fun deleteBooking(id: Int)
}