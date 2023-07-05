package com.travelsmartplus.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class SetupAccountRequest(
    val newPassword: String,
    val preferredAirlines: List<String>,
    val preferredHotelChains: List<String>
)