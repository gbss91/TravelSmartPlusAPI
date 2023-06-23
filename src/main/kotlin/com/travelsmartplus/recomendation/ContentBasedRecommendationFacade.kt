package com.travelsmartplus.recomendation

import com.travelsmartplus.models.FlightBooking

interface ContentBasedRecommendationFacade {
    fun recommend(preferences: List<Double>, flights: List<FlightBooking>): FlightBooking?
}