package com.travelsmartplus.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val orgId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val salt: String
)