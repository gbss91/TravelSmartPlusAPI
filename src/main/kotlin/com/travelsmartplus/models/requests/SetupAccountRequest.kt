package com.travelsmartplus.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class SetupAccountRequest(
    val newPassword: String,
    val preferredAirlines: Set<String>,
    val preferredHotelChains: Set<String>
)