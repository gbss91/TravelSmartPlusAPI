package com.travelsmartplus.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddUserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val admin: Boolean,
    val password: String
)