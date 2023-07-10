package com.travelsmartplus.functional

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.AddUserRequest
import com.travelsmartplus.models.requests.SetupAccountRequest
import com.travelsmartplus.testModule
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
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

    private lateinit var adminToken: String
    private lateinit var staffToken: String

    @Before
    fun setup() {
        DatabaseTestHelper.setup()
        adminToken = DatabaseTestHelper.signIn(email = "john@test.com", password = "myPass123")
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `admin creates new user`() = testApplication {
        application { testModule() }
        val user = AddUserRequest(
            firstName = "Sara",
            lastName = "Smith",
            email = "sara@test.com",
            admin = true,
            password = "myPass123"
        )
        val request = client.post("api/admin/new-user") {
            header(HttpHeaders.Authorization, "Bearer $adminToken")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(user))
        }
        println("ERROR: ${request.status.description}")
        assertEquals(HttpStatusCode.Created, request.status)
    }

    @Test
    fun `admin can get all company users`() = testApplication {
        application { testModule() }

        // Test get all users
        val response = client.get("api/admin/users/1") {
            header(HttpHeaders.Authorization, "Bearer $adminToken")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(response)

        // Test get one user
        val user = client.get("api/user/1") {
            header(HttpHeaders.Authorization, "Bearer $adminToken")
        }
        assertEquals(HttpStatusCode.OK, user.status)
        assertNotNull(user)
    }

    @Test
    fun `non-admin should not get all users`() = testApplication {
        application { testModule() }

        // Sign in as staff
        staffToken = DatabaseTestHelper.signIn(email = "jane@test.com", password = "myPass123")

        // Test get all users
        val response = client.get("api/admin/users/1") {
            header(HttpHeaders.Authorization, "Bearer $staffToken")
        }
        assertEquals(HttpStatusCode.Forbidden, response.status)
    }

    @Test
    fun `staff cannot access other profiles`() = testApplication {
        application { testModule() }

        // Sign in as staff
        staffToken = DatabaseTestHelper.signIn(email = "jane@test.com", password = "myPass123")

        //Try to get another user account
        val response = client.get("api/user/1") {
            header(HttpHeaders.Authorization, "Bearer $staffToken")
        }
        assertEquals(HttpStatusCode.Forbidden, response.status)

    }

    @Test
    fun `edit existing user`() = testApplication {
        application { testModule() }
        val editUser = User(
            orgId = 1,
            firstName = "Paula",
            lastName = "Smith",
            email = "sara@test.com",
            admin = true,
            password = "123456",
            salt = "123",
            accountSetup = true
        )
        val request = client.put("api/user/1") {
            header(HttpHeaders.Authorization, "Bearer $adminToken")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(editUser))
        }
        val responseBody = request.bodyAsText()
        assertEquals(HttpStatusCode.Created, request.status)
        assertNotNull(responseBody)
    }

    @Test
    fun `successfully delete an user`() = testApplication {
        application { testModule() }
        println("TOKEN: $adminToken")
        val request = client.delete("api/user/1") {
            header(HttpHeaders.Authorization, "Bearer $adminToken")
        }
        assertEquals(HttpStatusCode.OK, request.status)
    }

    @Test
    fun `setup account`() = testApplication {
        application { testModule() }
        val setupAccountRequest = SetupAccountRequest("MyNewPass1234!", listOf("AA", "EI"), listOf("MC", "AC"))
        val request = client.post("api/user/1/setup") {
            header(HttpHeaders.Authorization, "Bearer $adminToken")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(setupAccountRequest))
        }
        assertEquals(HttpStatusCode.Created, request.status)
    }
}
