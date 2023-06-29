package com.travelsmartplus.models.responses

import kotlinx.serialization.Serializable

/**
 * Represents the structure for the authentication response
 * @author Gabriel Salas
 * @property token The JWT authentication token.
 * @property refreshToken The refresh token for obtaining new authentication tokens.
 * @property accountSetup Identify new account that may require initial setup
 * */

@Serializable
data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val accountSetup: Boolean,
    val orgId: Int? = null
)
