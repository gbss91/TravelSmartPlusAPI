package com.travelsmartplus.utils

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.time.Duration

/**
 * Sets up the [HttpClient] used to make calls to external APIs
 * @author Gabriel Salas
 * @return The configured HttpClient object.
 * */

object HttpClientFactory {
    private val CONNECTION_TIMEOUT = Duration.ofSeconds(10) // 10 seconds
    private val READ_TIMEOUT = Duration.ofSeconds(20) // 20 seconds

    fun createHttpClient(): HttpClient {
        val client = HttpClient(OkHttp) {
            engine {
                config {
                    followRedirects(true)
                    connectTimeout(CONNECTION_TIMEOUT)
                    readTimeout(READ_TIMEOUT)
                }
            }
            install (ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                })
            }
        }
        return client
    }
}