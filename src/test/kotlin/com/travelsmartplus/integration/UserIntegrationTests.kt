package com.travelsmartplus.integration

import com.travelsmartplus.DatabaseTestHelper
<<<<<<< HEAD
import com.travelsmartplus.models.User
import com.travelsmartplus.module
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
=======
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.User
import com.travelsmartplus.utils.HashingService
import kotlinx.coroutines.runBlocking
>>>>>>> development
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
<<<<<<< HEAD
import kotlin.test.assertNotNull

class UserIntegrationTests {

    private lateinit var token: String
=======
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserIntegrationTests {

    private val dao = UserDAOFacadeImpl()
    private val hashingService = HashingService()

>>>>>>> development

    @Before
    fun setup() {
        DatabaseTestHelper.setup()
<<<<<<< HEAD
        token = DatabaseTestHelper.signIn(email = "john@test.com", password = "myPass123")
=======
>>>>>>> development
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
<<<<<<< HEAD
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
=======
    fun `get user by user Id`() = runBlocking {
        val user = dao.getUser(1)
        assertEquals(1, user?.id)
        assertEquals("John", user?.firstName)
        assertEquals("Ireland", user?.travelData?.nationality)
    }

    @Test
    fun `get all users for org`() = runBlocking {
        val users = dao.allUsers(1)
        assertTrue(users.isNotEmpty())
    }

    @Test
    fun `get user by email`() = runBlocking {
        val user = dao.getUserByEmail("john@test.com")
        assertNotNull(user)
        assertEquals(1, user.id)
        assertEquals("John", user.firstName)
    }

    @Test
    fun `add new user`() = runBlocking {
        val newUser = User(
            orgId = 1,
            firstName = "Test",
            lastName = "User",
            email = "test@example.com",
            admin = false,
            password = "myPass1234",
            salt = "salt",
            accountSetup = false
        )
        val user = dao.addUser(newUser)
        assertNotNull(user?.id)
        assertEquals("Test", user?.firstName)
    }

    @Test
    fun `edit user`() = runBlocking {

        val user = User(
            id = 1,
            orgId = 1,
            firstName = "Paula",
            lastName = "Doe",
            email = "john@test.com",
            admin = true,
            password = "23646131f8752ab2e9d65345cfc7b5d515af4661a15ba749922cb2e674c36d9d",
            salt = "e41ea5cc46b2b8b8099f81cd1e493bc6ad6f9d4d19fc149f37ca4ae154ba28f7",
            accountSetup = true
        )

        dao.editUser(user.id!!, user)

        val updatedUser = dao.getUser(1)
        assertNotNull(user)
        assertEquals("Paula", updatedUser?.firstName)
    }

    @Test
    fun `update password`() = runBlocking {

        val updatedPass = hashingService.generate("MyNewPass1234!")
        dao.updatePassword(1, updatedPass)

        val updatedUser = dao.getUser(1)
        assertNotEquals("23646131f8752ab2e9d65345cfc7b5d515af4661a15ba749922cb2e674c36d9d", updatedUser?.password)
    }

}
>>>>>>> development
