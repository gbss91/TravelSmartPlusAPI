package com.travelsmartplus.services

import com.travelsmartplus.services.Endpoints.AMADEUS_TEST_AUTH
import com.travelsmartplus.utils.HttpClientFactory
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class AmadeusAuthentication {

    private val amadeusKey = System.getenv("AMADEUS_KEY")
    private val amadeusSecret = System.getenv("AMADEUS_SECRET")

    suspend fun call(): String {
        return getAccessToken()
    }

    // Request access token to make API calls as per Amadeus documentation [https://developers.amadeus.com/self-service/apis-docs/guides/authorization-262]
    private suspend fun getAccessToken(): String {
        val client = HttpClientFactory.createHttpClient()

        try {
            val response = client.submitForm(
                url = AMADEUS_TEST_AUTH,
                formParameters = parameters {
                    append("grant_type", "client_credentials")
                    append("client_id", amadeusKey)
                    append("client_secret", amadeusSecret)
                }
            )

            if (response.status.isSuccess()) {
                val jsonObject = Json.parseToJsonElement(response.body()).jsonObject
                return jsonObject["access_token"].toString()
            } else {
                throw IllegalStateException("Failed to retrieve access token. Response status: ${response.status}")
            }
        } catch (e: Exception) {
            throw IllegalStateException("Error occurred while retrieving access token", e)
        } finally {
            client.close()
        }
    }
}