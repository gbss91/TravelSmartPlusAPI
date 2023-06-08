package com.travelsmartplus.dao.hotel

import com.travelsmartplus.models.HotelBooking

interface HotelBookingDAOFacade {
    suspend fun getHotelBooking(id: Int): HotelBooking?
    suspend fun addHotelBooking(hotelBooking: HotelBooking): Int
    suspend fun deleteHotelBooking(id: Int)
}