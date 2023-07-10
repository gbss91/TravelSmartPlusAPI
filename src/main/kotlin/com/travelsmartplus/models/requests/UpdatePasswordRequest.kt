package com.travelsmartplus.models.requests

import kotlinx.serialization.Serializable

/**
 * Represents the structure for password requests
 * @author Gabriel Salas
 */

@Serializable
data class UpdatePasswordRequest(
    val userId: Int,
    val newPassword: String
)