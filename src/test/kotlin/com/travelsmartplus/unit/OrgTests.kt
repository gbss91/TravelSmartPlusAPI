package com.travelsmartplus.unit

import com.travelsmartplus.models.Org
import org.junit.Test
import kotlin.test.assertEquals
<<<<<<< HEAD
import kotlin.test.assertNotNull
=======
import kotlin.test.assertNull
>>>>>>> development

class OrgTests {

    @Test
<<<<<<< HEAD
    fun testOrg() {
        // Test instance of org
        val org = Org(1, "Company Test", 12345)
        assertNotNull(org)

        // Test get org id
        assertEquals(1, org.id)

        // Test get Org name
        assertEquals("Company Test", org.orgName)
=======
    fun `test creating org with default values`() {
        val org = Org(orgName = "test org", duns = 12345)
        assertEquals(org.orgName, "test org")
        assertEquals(org.duns, 12345)
        assertEquals(org.id, 0) // Should be 0 by default
    }

    @Test
    fun `test creating org with provided id`() {
        val org = Org(id = 1, orgName = "test org", duns = 12345)
        assertEquals(org.id, 1)
        assertEquals(org.orgName, "test org")
        assertEquals(org.duns, 12345)
    }

    @Test
    fun `test creating org with null id`() {
        val org = Org(id = null, orgName = "test org", duns = 12345)
        assertNull(org.id)
        assertEquals(org.orgName, "test org")
        assertEquals(org.duns, 12345)
>>>>>>> development
    }
}
