package com.travelsmartplus.models.responses

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

/**
 * Represents a user session. Sessions are use to persist the sign in and help with the token refresh process.
 * @author Gabriel Salas
 * @property userId The ID of the user associated with the session.
 */

@Serializable
data class UserSession(val userId: Int?) : Principal
