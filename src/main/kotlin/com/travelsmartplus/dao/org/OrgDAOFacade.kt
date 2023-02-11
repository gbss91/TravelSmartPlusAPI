package com.travelsmartplus.dao.org

import com.travelsmartplus.models.Org
import com.travelsmartplus.models.Orgs

interface OrgDAOFacade {
    suspend fun getOrg(id: Int): Org?
    suspend fun getAllOrgs(): List<Org>
    suspend fun getOrgByName(orgName: String): Org?
    suspend fun getOrgByDuns(duns: Int): Org?
    suspend fun addOrg(org: Org): Org?
    suspend fun editOrg(id: Int, orgName: String, duns: Int)
    suspend fun deleteOrg(id: Int)

}