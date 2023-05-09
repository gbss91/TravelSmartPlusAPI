package com.travelsmartplus.models.requests

import com.travelsmartplus.models.Airport
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class BookingSearchRequest(
    val oneWay: Boolean,
    val origin: Airport,
    val destination: Airport,
    val departureDate: LocalDate,
    val returnDate: LocalDate?,
    val adultsNumber: Int,
    val travelClass: String
)
