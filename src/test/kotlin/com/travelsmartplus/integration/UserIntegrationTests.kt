package com.travelsmartplus.integration

import com.travelsmartplus.models.User
import com.travelsmartplus.module
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.*

class UserIntegrationTests {

    //Variables used for all tests
    companion object {
        private val user = User(orgId = 1, firstName = "Sara", lastName = "Smith" , email = "sara@test.com", password = "123456", salt = "123")
        private var userId: Int? = null
    }

    @Test
    fun createUser() = testApplication{
        application { module() }
        val request = client.post("api/user") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(user))
        }
        assertEquals(HttpStatusCode.Accepted, request.status)

        //Store user ID for other tests
        userId = request.body()
    }

    @Test
    fun testGetUsers() = testApplication{
        application { module() }

        //Test get all users
        val response = client.get("api/users/1")
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(response)

        //Test get one user
        val user =  client.get("api/user/$userId")
        assertEquals(HttpStatusCode.OK, user.status)
        assertNotNull(user)
    }

    @Test
    fun editUser() = testApplication {
        application { module() }
        val editUser = User(orgId = 1, firstName = "Paula", lastName = "Smith" , email = "sara@test.com", password = "123456", salt = "123")
        val request = client.post("api/user/$userId") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(editUser))
        }
        assertEquals(HttpStatusCode.Accepted, request.status)
    }

    @Test
    fun deleteUser() = testApplication{
        application { module() }
        val request = client.delete("api/user/$userId")
        assertEquals(HttpStatusCode.OK, request.status)
    }
}
