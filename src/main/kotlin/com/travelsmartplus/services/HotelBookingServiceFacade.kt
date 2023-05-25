package com.travelsmartplus.services

import com.travelsmartplus.models.HotelBooking
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.services.apiResponses.AmadeusHotelListResponse

interface HotelBookingServiceFacade {
    suspend fun getHotels(bookingSearchRequest: BookingSearchRequest): List<HotelBooking>
    suspend fun getHotelIds(response: AmadeusHotelListResponse): List<String>
}