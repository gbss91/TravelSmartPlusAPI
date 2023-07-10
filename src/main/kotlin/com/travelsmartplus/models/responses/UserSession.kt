package com.travelsmartplus.models.responses

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

<<<<<<< HEAD
// Data class for session
=======
/**
 * Represents a user session. Sessions are used to persist the sign in and help with the token refresh process.
 * @author Gabriel Salas
 * @property userId The ID of the user associated with the session.
 */

>>>>>>> development
@Serializable
data class UserSession(val userId: Int?) : Principal
