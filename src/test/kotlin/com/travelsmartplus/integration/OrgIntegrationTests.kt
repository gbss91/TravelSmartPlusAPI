package com.travelsmartplus.integration
<<<<<<< HEAD

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.models.Org
import com.travelsmartplus.module
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
=======
import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.dao.org.OrgDAOFacadeImpl
import com.travelsmartplus.models.Org
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
>>>>>>> development
import kotlin.test.assertNotNull

class OrgIntegrationTests {

<<<<<<< HEAD
    private lateinit var token: String
=======
    private val dao = OrgDAOFacadeImpl()

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
=======
    fun `get org using Id`() = runBlocking {
        val org = dao.getOrg(1)
        assertEquals(1, org?.id)
        assertEquals("Test Company", org?.orgName)
    }

    @Test
    fun `get all orgs`() = runBlocking {
        val orgs = dao.getAllOrgs()
        assertEquals(1, orgs.size)
    }

    @Test
    fun `get org using DUNS number`() = runBlocking {
        val org = dao.getOrgByDuns(123456789)
        assertEquals(1, org?.id)
    }

    @Test
    fun `add org`() = runBlocking {
        val newOrg = Org(
            orgName = "My Org",
            duns = 244545549
        )
        val org = dao.addOrg(newOrg)
        assertNotNull(org)
        assertEquals("My Org", org.orgName)
    }

    @Test
    fun `delete org`() = runBlocking {
        dao.deleteOrg(1)
        assertEquals(null, dao.getOrg(1))
    }
}
>>>>>>> development
