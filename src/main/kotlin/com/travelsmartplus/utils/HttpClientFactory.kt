package com.travelsmartplus.utils

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Sets ups the [HttpClient] used to make calls to external APIs
 * @author Gabriel Salas
 * @return The configured HttpClient object.
 * */

object HttpClientFactory {

    fun createHttpClient(): HttpClient {
        val client = HttpClient() {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                })
            }
        }
        return client
    }
}