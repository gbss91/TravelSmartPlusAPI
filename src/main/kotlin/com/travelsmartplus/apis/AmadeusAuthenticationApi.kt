package com.travelsmartplus.apis

import com.travelsmartplus.apis.Endpoints.AMADEUS_TEST_AUTH
import com.travelsmartplus.utils.HttpClientFactory
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

/**
 * Retrieves a token from Amadeus API. This token is used for all requests
 * as per [Amadeus documentation](https://developers.amadeus.com/self-service/apis-docs/guides/authorization-262)
 * @author Gabriel Salas
 * @return A [String] with the token
 * @throws IllegalStateException if there is an error retrieving token or the API response indicates failure.
 */

class AmadeusAuthenticationApi {

    private val amadeusKey = System.getenv("AMADEUS_KEY")
    private val amadeusSecret = System.getenv("AMADEUS_SECRET")
    private val client = HttpClientFactory.createHttpClient()

    suspend fun call(): String {
        return getAccessToken()
    }

    // Request access token
    private suspend fun getAccessToken(): String {
        try {
            val response = client.submitForm(
                url = AMADEUS_TEST_AUTH,
                formParameters = Parameters.build {
                    append("grant_type", "client_credentials")
                    append("client_id", amadeusKey)
                    append("client_secret", amadeusSecret)
                },
                encodeInQuery = false
            )

            if (response.status.isSuccess()) {
                val jsonObject = Json.parseToJsonElement(response.body()).jsonObject
                return jsonObject["access_token"].toString().removeSurrounding("\"")
            } else {
                throw IllegalStateException("Failed to retrieve access token. Response status: ${response.status}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Error occurred while retrieving access token", e)
        } finally {
            client.close()
        }
    }
}