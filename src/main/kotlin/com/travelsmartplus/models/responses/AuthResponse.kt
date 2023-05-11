package com.travelsmartplus.models.responses

import kotlinx.serialization.Serializable

/**
 * Represents the structure for the authentication response
 * @author Gabriel Salas
 * @property token The JWT authentication token.
 * @property refreshToken The refresh token for obtaining new authentication tokens.
 * */

@Serializable
data class AuthResponse(
    val token: String,
    val refreshToken: String
)
