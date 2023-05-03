package com.travelsmartplus.models.responses

object HttpResponses {

    const val DUPLICATE_ORG = "Organisation already exists. Please contact administrator."
    const val FAILED_CREATE_ORG = "Failed to create organisation. Please try again later."
    const val FAILED_EDIT_ORG = "Failed to edit organisation. Please try again later."
    const val FAILED_DELETE_ORG = "Failed to delete organisation. Please try again later."

    const val DUPLICATE_USER = "User already exists. Please sign in."
    const val FAILED_CREATE_USER = "Failed to create user. Please try again later."
    const val FAILED_EDIT_USER = "Failed to edit user. Please try again later."
    const val FAILED_DELETE_USER = "Failed to delete user. Please try again later."

    const val FAILED_SIGNUP = "Failed to sign up. Please try again later."
    const val FAILED_SIGNIN = "Failed to sign in. Please try again later."
    const val INVALID_CREDENTIALS = "Incorrect username or password."
    const val UNAUTHORIZED = "Unauthorised. Please sign in."
    const val INTERNAL_SERVER_ERROR = "Internal Server Error. Please try again later."

}