package com.travelsmartplus.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val orgName: String,
    val duns: Int
)
