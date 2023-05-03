package com.travelsmartplus.utils

import com.travelsmartplus.models.requests.SignInRequest
import com.travelsmartplus.models.requests.SignUpRequest

object Validator {

    fun validateSignUpRequest(request: SignUpRequest) {
        if (request.firstName.isBlank() || request.lastName.isBlank() || request.email.isBlank() || request.password.isBlank() || request.orgName.isBlank() || request.duns.toString().isBlank()) {
            throw IllegalArgumentException("Missing fields")
        }
        if (request.password.length < 8) {
            throw IllegalArgumentException("Password must be at least 8 characters long")
        }
    }

    fun validateSignInRequest(request: SignInRequest) {
        if (request.email.isBlank() || request.password.isBlank()) {
            throw IllegalArgumentException("Missing fields")
        }
    }
}
