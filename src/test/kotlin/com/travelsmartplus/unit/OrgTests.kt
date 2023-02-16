package com.travelsmartplus.unit

import com.travelsmartplus.models.Org
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class OrgTests {

    @Test
    fun testOrg() {

        //Test instance of org
        val org = Org(1, "Company Test", 12345)
        assertNotNull(org)

        //Test get org id
        assertEquals(1, org.id)

        //Test get Org name
        assertEquals("Company Test", org.orgName)
    }
}