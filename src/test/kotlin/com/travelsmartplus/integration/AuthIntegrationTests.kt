package com.travelsmartplus.integration

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.models.requests.SignInRequest
import com.travelsmartplus.models.requests.SignUpRequest
import com.travelsmartplus.module
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AuthIntegrationTests {

    @Before
    fun setup() {
        DatabaseTestHelper.setup()
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `duplicate user sign up`() = testApplication {
        application { module() }
        val signUpRequest = SignUpRequest(
            firstName = "John",
            lastName = "Doe",
            email = "john@test.com",
            password = "myPass123",
            orgName = "My Org",
            duns = 1234567
        )
        val request = client.post("api/signup") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(signUpRequest))
        }
        assertEquals(HttpStatusCode.Conflict, request.status)
    }

    @Test
    fun `successful sign up`() = testApplication {
        application { module() }
        val signUpRequest = SignUpRequest(
            firstName = "Sara",
            lastName = "Smith",
            email = "sara@test.com",
            password = "12345678",
            orgName = "Sara Org",
            duns = 777777
        )
        val request = client.post("api/signup") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(signUpRequest))
        }
        assertEquals(HttpStatusCode.Created, request.status)
    }

    @Test
    fun `sign in with wrong password`() = testApplication {
        application { module() }
        val signInRequest = SignInRequest(email = "john@test.com", password = "wrongPass")
        val request = client.post("api/signin") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(signInRequest))
        }
        assertEquals(HttpStatusCode.Unauthorized, request.status)
    }

    @Test
    fun `successful sign in`() = testApplication {
        application { module() }
        val signInRequest = SignInRequest(email = "john@test.com", password = "myPass123")
        val request = client.post("api/signin") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(signInRequest))
        }
        assertEquals(HttpStatusCode.OK, request.status)
    }
}
