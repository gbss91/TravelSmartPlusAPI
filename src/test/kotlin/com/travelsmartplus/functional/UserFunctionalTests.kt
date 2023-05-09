package com.travelsmartplus.functional

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.models.User
import com.travelsmartplus.module
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserFunctionalTests {

    private lateinit var token: String

    @Before
    fun setup() {
        DatabaseTestHelper.setup()
        token = DatabaseTestHelper.signIn(email = "john@test.com", password = "myPass123")
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `create new user`() = testApplication {
        application { module() }
        val user = User(
            orgId = 1,
            firstName = "Sara",
            lastName = "Smith",
            email = "sara@test.com",
            admin = true,
            password = "myPass123",
            salt = ""
        )
        val request = client.post("api/user") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(user))
        }
        assertEquals(HttpStatusCode.Created, request.status)
    }

    @Test
    fun `get users`() = testApplication {
        application { module() }

        // Test get all users
        val response = client.get("api/users/1") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(response)

        // Test get one user
        val user = client.get("api/user/1") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        assertEquals(HttpStatusCode.OK, user.status)
        assertNotNull(user)
    }

    @Test
    fun `edit existing user`() = testApplication {
        application { module() }
        val editUser = User(
            orgId = 1,
            firstName = "Paula",
            lastName = "Smith",
            email = "sara@test.com",
            admin = true,
            password = "123456",
            salt = "123"
        )
        val request = client.post("api/user/1") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(editUser))
        }
        assertEquals(HttpStatusCode.Created, request.status)
    }

    @Test
    fun `successfully delete an user`() = testApplication {
        application { module() }
        val request = client.delete("api/user/1") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        assertEquals(HttpStatusCode.OK, request.status)
    }
}
