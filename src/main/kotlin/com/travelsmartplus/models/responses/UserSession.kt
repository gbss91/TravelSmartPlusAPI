package com.travelsmartplus.models.responses

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

// Data class for session
@Serializable
data class UserSession(val userId: Int?) : Principal
