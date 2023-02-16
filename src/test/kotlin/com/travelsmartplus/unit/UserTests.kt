package com.travelsmartplus.unit

import com.travelsmartplus.models.User
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserTests {

    @Test
    fun testUser() {

        //Test instance of user
        val user = User(1, 15, "Paul", "Smith", "paul@test.com", "14f675f", "12345")
        assertNotNull(user)

        //Test get userId
        assertEquals(1, user.id)

        //Test get email
        assertEquals("paul@test.com", user.email)

    }

}