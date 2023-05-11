package com.travelsmartplus.models.requests

import kotlinx.serialization.Serializable

/**
 * Represents the structure of an authentication request made by the user.
 * @author Gabriel Salas
 * @property refreshToken The refresh token used to request a new authentication token.
 */

@Serializable
data class AuthRequest(
    val refreshToken: String
)
