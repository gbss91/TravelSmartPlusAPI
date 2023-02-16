package com.travelsmartplus.integration

import com.travelsmartplus.models.Org
import com.travelsmartplus.models.User
import com.travelsmartplus.module
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class OrgIntegrationTests {

    //Variables used for all tests
    companion object {
        private val org = Org(orgName = "My Org Inc", duns = 123456)
        private var orgId: Int? = null
    }

    @Test
    fun createOrg() = testApplication {
        application { module() }
        val request = client.post("api/org") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(org))
        }
        assertEquals(HttpStatusCode.Accepted, request.status)

        //Store org ID for other tests
        orgId = request.body()
    }

    @Test
    fun getOrg() = testApplication {
        application { module() }
        val response = client.get("api/org/$orgId")
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(response)

    }

    @Test
    fun deleteOrg() = testApplication {
        application { module() }
        val request = client.delete("api/org/$orgId")
        assertEquals(HttpStatusCode.OK, request.status)
    }

}