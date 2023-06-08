package com.travelsmartplus.functional

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.models.requests.SignInRequest
import com.travelsmartplus.models.requests.SignUpRequest
import com.travelsmartplus.models.requests.UpdatePasswordRequest
import com.travelsmartplus.testModule
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AuthFunctionalTests {

    private lateinit var token: String

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
        application { testModule() }
        val signUpRequest = SignUpRequest(
            firstName = "John",
            lastName = "Doe",
            email = "john@test.com",
            password = "myPass123",
            orgName = "Test Org",
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
        application { testModule() }
        val signUpRequest = SignUpRequest(
            firstName = "Paula",
            lastName = "Smith",
            email = "paula@test.com",
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
        application { testModule() }
        val signInRequest = SignInRequest(email = "john@test.com", password = "wrongPass")
        val request = client.post("api/signin") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(signInRequest))
        }
        assertEquals(HttpStatusCode.Unauthorized, request.status)
    }

    @Test
    fun `successful sign in`() = testApplication {
        application { testModule() }
        val signInRequest = SignInRequest(email = "john@test.com", password = "myPass123")
        val request = client.post("api/signin") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(signInRequest))
        }
        assertEquals(HttpStatusCode.OK, request.status)
    }

    @Test
    fun `successful password update`() = testApplication {
        application { testModule() }
        val token = DatabaseTestHelper.signIn(email = "john@test.com", password = "myPass123")
        val updatePasswordRequest = UpdatePasswordRequest(userId = 1, newPassword = "myNewPass1234")
        val request = client.post("api/update-password") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(updatePasswordRequest))
        }
        assertEquals(HttpStatusCode.Created, request.status)
    }
}
