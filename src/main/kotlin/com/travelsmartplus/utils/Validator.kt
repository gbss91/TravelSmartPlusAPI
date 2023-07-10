package com.travelsmartplus.utils

<<<<<<< HEAD
import com.travelsmartplus.models.requests.SignInRequest
import com.travelsmartplus.models.requests.SignUpRequest
=======
import com.travelsmartplus.models.requests.SetupAccountRequest
import com.travelsmartplus.models.requests.SignUpRequest
import com.travelsmartplus.models.requests.UpdatePasswordRequest

/**
 * Validator provides functions for validating requests.
 * @author Gabriel Salas
 */
>>>>>>> development

object Validator {

    fun validateSignUpRequest(request: SignUpRequest) {
        if (request.firstName.isBlank() || request.lastName.isBlank() || request.email.isBlank() || request.password.isBlank() || request.orgName.isBlank() || request.duns.toString().isBlank()) {
            throw IllegalArgumentException("Missing fields")
        }
        if (request.password.length < 8) {
            throw IllegalArgumentException("Password must be at least 8 characters long")
        }
    }

<<<<<<< HEAD
    fun validateSignInRequest(request: SignInRequest) {
        if (request.email.isBlank() || request.password.isBlank()) {
            throw IllegalArgumentException("Missing fields")
        }
=======
    fun validateUpdatePasswordRequest(request: UpdatePasswordRequest) {
        if (request.newPassword.isBlank()) {
            throw IllegalArgumentException("Missing fields")
        }
        if (request.newPassword.length < 8) {
            throw IllegalArgumentException("Password must be at least 8 characters long")
        }
    }

    fun validateSetupAccountRequest(request: SetupAccountRequest) {
        if (request.newPassword.isBlank() || request.preferredAirlines.isEmpty() || request.preferredHotelChains.isEmpty()) {
            throw IllegalArgumentException("Missing fields")
        }
        if (request.newPassword.length < 8) {
            throw IllegalArgumentException("Password must be at least 8 characters long")
        }
>>>>>>> development
    }
}
