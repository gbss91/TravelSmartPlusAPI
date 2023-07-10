package com.travelsmartplus.integration

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.User
import com.travelsmartplus.utils.HashingService
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserIntegrationTests {

    private val dao = UserDAOFacadeImpl()
    private val hashingService = HashingService()


    @Before
    fun setup() {
        DatabaseTestHelper.setup()
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
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