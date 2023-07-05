package com.travelsmartplus.recomendation

import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.HotelBooking

interface KNNRecommendationFacade {
    suspend fun trainModel(previousBookings: List<Booking>, preferredAirlines: List<String>, preferredHotels: List<String>)
    fun predict(flights: List<FlightBooking>): FlightBooking?
    fun predict(hotels: List<HotelBooking>): HotelBooking?
}