package com.travelsmartplus.apis

import com.travelsmartplus.apis.Endpoints.PLACE_SEARCH
import com.travelsmartplus.apis.apiResponses.FindPlaceResponse
import com.travelsmartplus.utils.HttpClientFactory
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import kotlinx.serialization.json.Json
import kotlin.Exception

/**
 * Retrieves booking image Urls from [Google Places API](https://developers.google.com/maps/documentation/places/web-service)
 * based on photo reference
 * @author Gabriel Salas
 * @return An image URL
 * @throws IllegalStateException if there is an error retrieving flights or the API response indicates failure.
 */

class PlacesApi {

    private val client = HttpClientFactory.createHttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    private val apiKey = System.getenv("PLACES_API_KEY")

    // Gets the photo reference from Google Places API - This is used to search for URL
    suspend fun getImageUrl(input: String): String {
        try {
            val response = client.get(PLACE_SEARCH) {
                url {
                    parameters.append("fields", "photos")
                    parameters.append("input", input)
                    parameters.append("inputtype", "textquery")
                    parameters.append("key", apiKey)
                }
            }

            if (response.status.isSuccess()) {
                val photoReference = json.decodeFromString<FindPlaceResponse>(response.body()).candidates[0].photos[0].photoReference
                return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference=${photoReference}&key=${apiKey}"
            } else {
                throw IllegalStateException("Failed to retrieve photo reference. Response status: ${response.status}")
            }
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            throw IllegalStateException("Connection timeout", e)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Error occurred while retrieving image URL", e)
        }
    }

}