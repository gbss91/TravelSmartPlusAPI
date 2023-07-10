package com.travelsmartplus.integration
import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.dao.org.OrgDAOFacadeImpl
import com.travelsmartplus.models.Org
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class OrgIntegrationTests {

    private val dao = OrgDAOFacadeImpl()


    @Before
    fun setup() {
        DatabaseTestHelper.setup()
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
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