package com.travelsmartplus.services

import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.HotelBooking

interface PlacesServiceFacade {
    suspend fun processBookingsImage(allBookings: List<Booking>): List<Booking>
    suspend fun getAddress(hotelBookings: List<HotelBooking>): List<HotelBooking>
}