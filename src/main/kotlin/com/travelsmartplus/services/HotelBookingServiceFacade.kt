package com.travelsmartplus.services

import com.travelsmartplus.apis.apiResponses.AmadeusHotelListResponse
import com.travelsmartplus.models.HotelBooking
import com.travelsmartplus.models.requests.BookingSearchRequest

interface HotelBookingServiceFacade {
    suspend fun getHotels(bookingSearchRequest: BookingSearchRequest): List<HotelBooking>
    suspend fun getHotelIds(response: AmadeusHotelListResponse): List<String>
}