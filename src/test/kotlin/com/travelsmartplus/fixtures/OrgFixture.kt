package com.travelsmartplus.fixtures

import com.travelsmartplus.models.Org

object OrgFixture {
    val orgs = listOf(
        Org(
            id= 1,
            orgName = "Test Company",
            duns = 123456789
        )
    )

}