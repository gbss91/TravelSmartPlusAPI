package com.travelsmartplus.apis

/**
 * Object with all the URLs used in the APIs
 * @author Gabriel Salas
*/

object Endpoints {
    private const val AMADEUS_TEST_BASE_URL = "https://api.amadeus.com"
    const val AMADEUS_TEST_AUTH = "https://api.amadeus.com/v1/security/oauth2/token"
    const val SEARCH_FLIGHT = "$AMADEUS_TEST_BASE_URL/v2/shopping/flight-offers"
    const val HOTEL_LIST = "$AMADEUS_TEST_BASE_URL/v1/reference-data/locations/hotels/by-city"
    const val SEARCH_HOTEL = "$AMADEUS_TEST_BASE_URL/v3/shopping/hotel-offers"
    const val PLACE_SEARCH = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json"
    const val ADDRESS_SEARCH = "https://maps.googleapis.com/maps/api/geocode/json"
}