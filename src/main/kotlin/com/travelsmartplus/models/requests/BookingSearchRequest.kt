package com.travelsmartplus.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class BookingSearchRequest(
    val originCity: String,
    val originIataCode: String,
    val destinationCity: String,
    val destinationIataCode: String,
    val departureDate: String,
    val returnDate: String?,
    val adultsNumber: Int,
    val bookingClass: String
)
