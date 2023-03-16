package com.travelsmartplus.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val refreshToken: String
)
