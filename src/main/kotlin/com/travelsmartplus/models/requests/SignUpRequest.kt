package com.travelsmartplus.models.requests

import kotlinx.serialization.Serializable

<<<<<<< HEAD
=======
/**
 * Represents a sign-up request made by user.
 * @author Gabriel Salas
 * @property firstName The first name of the user signing up.
 * @property lastName The last name of the user signing up.
 * @property email The email of the user signing up.
 * @property password The password of the user signing up.
 * @property orgName The organization name for the user signing up.
 * @property duns The unique DUNS (Data Universal Numbering System) number for the organization.
 */

>>>>>>> development
@Serializable
data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val orgName: String,
    val duns: Int
)
