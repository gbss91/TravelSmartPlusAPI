package com.travelsmartplus.models.responses

/**
 * Object with the HTTP Response messages returned by all project routes
 * @author Gabriel Salas
 */

object HttpResponses {

    const val DUPLICATE_ORG = "Organisation already exists. Please contact the administrator."
    const val FAILED_CREATE_ORG = "Failed to create organisation. Please try again later."
    const val FAILED_DELETE_ORG = "Failed to delete organisation. Please try again later."

    const val DUPLICATE_USER = "User already exists. Please sign in."
    const val DUPLICATE_ADD_USER = "Email address already in use"
    const val FAILED_CREATE_USER = "Failed to create user. Please try again later."
    const val FAILED_EDIT_USER = "Failed to edit user. Please try again later."
    const val FAILED_DELETE_USER = "Failed to delete user. Please try again later."
    const val FAILED_SETUP = "Failed to set up account. Please try again later."

    const val FAILED_SIGNUP = "Failed to sign up. Please try again later."
    const val FAILED_SIGNIN = "Failed to sign in. Please try again later."
    const val INVALID_CREDENTIALS = "Incorrect username or password."
    const val FAILED_PASSWORD_UPDATE = "Failed to update password. Please try again later."

    const val UNAUTHORIZED = "Unauthorised. Please sign in."
    const val FORBIDDEN = "You are not authorised to perform this request."
    const val INTERNAL_SERVER_ERROR = "Internal Server Error. Please try again later."
    const val BAD_REQUEST = "Invalid request format."
    const val NOT_FOUND = "Resource not found. Please review request and try again."

}