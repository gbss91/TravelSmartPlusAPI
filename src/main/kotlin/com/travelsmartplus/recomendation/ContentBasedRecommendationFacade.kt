package com.travelsmartplus.recomendation

import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.HotelBooking

interface ContentBasedRecommendationFacade {
    suspend fun recommendFlights(preferences: List<String>, flights: List<FlightBooking>): FlightBooking?
    suspend fun recommendHotels(preferences: List<String>, hotels: List<HotelBooking>): HotelBooking?
}
