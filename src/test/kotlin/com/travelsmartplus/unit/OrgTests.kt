package com.travelsmartplus.unit

import com.travelsmartplus.models.Org
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OrgTests {

    @Test
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
    }
}
