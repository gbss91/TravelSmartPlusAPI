package com.travelsmartplus.models.requests

import kotlinx.serialization.Serializable

/**
 * Represents a sign-in request made by user.
 *  @author Gabriel Salas
 * @property email The email of the user attempting to sign in.
 * @property password The password of the user attempting to sign in.
 */

@Serializable
data class SignInRequest(
    val email: String,
    val password: String
)
