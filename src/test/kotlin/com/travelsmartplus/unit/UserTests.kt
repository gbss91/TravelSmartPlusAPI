package com.travelsmartplus.unit

import com.travelsmartplus.fixtures.TravelDataFixtures
import com.travelsmartplus.models.User
import org.junit.Test
import kotlin.test.assertEquals

class UserTests {

    @Test
    fun `create user with required values`() {
        val user = User(orgId = 1, firstName = "John", lastName = "Doe", email = "johndoe@example.com", admin = false, password = "password", salt = "salt", accountSetup = true)
        assertEquals(1, user.orgId)
        assertEquals("John", user.firstName)
        assertEquals("Doe", user.lastName)
        assertEquals("johndoe@example.com", user.email)
        assertEquals(false, user.admin)
        assertEquals("password", user.password)
        assertEquals("salt", user.salt)
    }

    @Test
    fun `create user without id`() {
        val user = User(orgId = 1, firstName = "John", lastName = "Doe", email = "johndoe@example.com", admin = false, password = "password", salt = "salt", accountSetup = true)
        assertEquals(0, user.id) // Default ID is 0
        assertEquals(1, user.orgId)
        assertEquals("John", user.firstName)
        assertEquals("Doe", user.lastName)
        assertEquals("johndoe@example.com", user.email)
        assertEquals(false, user.admin)
        assertEquals("password", user.password)
        assertEquals("salt", user.salt)
    }

    @Test
    fun `create user with travel details`() {
        val user = User(orgId = 1, firstName = "John", lastName = "Doe", email = "johndoe@example.com", admin = false, password = "password", salt = "salt", accountSetup = true, travelData = TravelDataFixtures.travelDetails[0])
        assertEquals(0, user.id) // Default ID is 0
        assertEquals(1, user.orgId)
        assertEquals("John", user.firstName)
        assertEquals("Doe", user.lastName)
        assertEquals("johndoe@example.com", user.email)
        assertEquals(false, user.admin)
        assertEquals("password", user.password)
        assertEquals("salt", user.salt)
        assertEquals("Ireland", user.travelData?.nationality)
    }
}
