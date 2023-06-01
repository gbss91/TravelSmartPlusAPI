package com.travelsmartplus.recomendation

import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.HotelBooking

interface KNNRecommendationFacade {
    suspend fun trainModel(previousBookings: List<Booking>, k: Int)
    fun predict(flights: List<FlightBooking>): FlightBooking?
    fun predict(hotels: List<HotelBooking>): HotelBooking?
}