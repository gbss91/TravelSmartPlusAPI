package com.travelsmartplus.functional

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.models.Org
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

class OrgFunctionalTests {

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
    fun `create org`() = testApplication {
        application { module() }
        val org = Org(orgName = "My Org Inc", duns = 123456)
        val request = client.post("api/org") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(org))
        }
        assertEquals(HttpStatusCode.Created, request.status)
    }

    @Test
    fun `get org using id`() = testApplication {
        application { module() }
        val response = client.get("api/org/1") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(response)
    }

    @Test
    fun `successfully delete org`() = testApplication {
        application { module() }
        val request = client.delete("api/org/1") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        assertEquals(HttpStatusCode.OK, request.status)
    }
}
