package com.travelsmartplus.apis

import com.travelsmartplus.apis.Endpoints.HOTEL_LIST
import com.travelsmartplus.apis.Endpoints.SEARCH_HOTEL
import com.travelsmartplus.apis.apiResponses.AmadeusHotelListResponse
import com.travelsmartplus.apis.apiResponses.AmadeusHotelOffersResponse
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.utils.HttpClientFactory
import io.ktor.client.call.*
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import kotlinx.serialization.json.Json

/**
 * Retrieves hotels from [Amadeus Hotel API](https://developers.amadeus.com/self-service/category/hotels)
 * based on the search parameters provided by user.
 * @author Gabriel Salas
 * @return [AmadeusHotelListResponse] and [AmadeusHotelOffersResponse]
 * @throws IllegalStateException if there is an error retrieving hotels or the API response indicates failure.
 */

class HotelApi {
    private val client = HttpClientFactory.createHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    // Get the list with hotel Ids in a city
    suspend fun getHotelList(token: String, bookingSearchRequest: BookingSearchRequest): AmadeusHotelListResponse {
        try {
            val response = client.get(HOTEL_LIST) {
                url {
                    parameters.append("cityCode", bookingSearchRequest.destination.iataCode)
                    parameters.append("ratings", listOf("2", "3").toString())
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            if (response.status.isSuccess()) {
                return json.decodeFromString<AmadeusHotelListResponse>(response.body())
            } else {
                throw IllegalStateException("Failed to retrieve hotel list. Response status: ${response.status}")
            }
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            throw IllegalStateException("Connection timeout", e)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Error occurred while retrieving hotel list", e)
        }
    }

    // Search hotel offers using hotel Ids. This includes the hotel details and rate
    suspend fun getHotelOffers(
        token: String,
        hotelIds: List<String>,
        bookingSearchRequest: BookingSearchRequest
    ): AmadeusHotelOffersResponse {
        try {

            val hotels = hotelIds.joinToString(",")

            val response = client.get(SEARCH_HOTEL) {
                url {
                    encodedParameters.append("hotelIds", hotels)
                    parameters.append("adults", bookingSearchRequest.adultsNumber.toString())
                    parameters.append("checkInDate", bookingSearchRequest.checkInDate.toString())
                    parameters.append("checkOutDate", bookingSearchRequest.checkOutDate.toString())
                    parameters.append("roomQuantity", 1.toString())
                    parameters.append("currency", "EUR")
                    parameters.append("bestRateOnly", true.toString())
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            if (response.status.isSuccess()) {
                return json.decodeFromString<AmadeusHotelOffersResponse>(response.body())
            } else {
                throw IllegalStateException("Failed to retrieve hotel offers. Response status: ${response.status}")
            }
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            throw IllegalStateException("Connection timeout", e)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Error occurred while retrieving hotel offers", e)
        }
    }

}