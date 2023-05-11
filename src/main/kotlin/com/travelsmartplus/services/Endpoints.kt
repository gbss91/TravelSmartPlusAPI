package com.travelsmartplus.services

/**
 * Object with all the URLs used in the APIs
 * @author Gabriel Salas
*/

object Endpoints {
    val AMADEUS_TEST_BASE_URL = "https://test.api.amadeus.com/v2"
    val AMADEUS_TEST_AUTH = "https://test.api.amadeus.com/v1/security/oauth2/token"
    val SEARCH_FLIGHT = "$AMADEUS_TEST_BASE_URL/shopping/flight-offers"
}