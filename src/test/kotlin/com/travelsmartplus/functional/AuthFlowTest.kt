package com.travelsmartplus.functional

import com.travelsmartplus.models.User
import com.travelsmartplus.module
import io.ktor.server.testing.*
import org.junit.Test

class AuthFlowTest {

    //Variables used for all tests
    companion object {
        private val user = User(orgId = 1, firstName = "Sara", lastName = "Smith" , email = "sara@test.com", password = "123456", salt = "123")
        private var userId: Int? = null
    }

    @Test
    fun testSignup() = testApplication {
        application { module() }

    }

    @Test
    fun testSignin() = testApplication {


    }

}