package com.travelsmartplus.apis

import com.travelsmartplus.apis.Endpoints.SEARCH_FLIGHT
import com.travelsmartplus.apis.apiResponses.AmadeusFlightOffersResponse
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.utils.HttpClientFactory
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

/**
 * Retrieves flights from the [Amadeus Flight API](https://developers.amadeus.com/self-service/category/flights)
 * based on the search parameters provided by user
 * @author Gabriel Salas
 * @return A list with all the flight offers retrieved from the API.
 * @throws IllegalStateException if there is an error retrieving flights or the API response indicates failure.
 */

class FlightApi {
    private val client = HttpClientFactory.createHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getFlights(token: String, bookingSearchRequest: BookingSearchRequest): AmadeusFlightOffersResponse {
        try {
            val response = client.get(SEARCH_FLIGHT) {
                url {
                    parameters.append("originLocationCode", bookingSearchRequest.origin.iataCode)
                    parameters.append("destinationLocationCode", bookingSearchRequest.destination.iataCode)
                    parameters.append("departureDate", bookingSearchRequest.departureDate.toString())
                    if (!bookingSearchRequest.oneWay) {
                        parameters.append(
                            "returnDate",
                            bookingSearchRequest.returnDate.toString()
                        ) // Only for return bookings
                    }
                    parameters.append("adults", bookingSearchRequest.adultsNumber.toString())
                    parameters.append("travelClass", bookingSearchRequest.travelClass)
                    parameters.append("nonStop", bookingSearchRequest.nonStop.toString())
                    parameters.append("currencyCode", "EUR")
                    parameters.append("max", 5.toString()) // 5 for testing only
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            if (response.status.isSuccess()) {
                return json.decodeFromString<AmadeusFlightOffersResponse>(response.body())
            } else {
                throw IllegalStateException("Failed to retrieve flights. Response status: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Error occurred while retrieving flights", e)
        }
    }
}