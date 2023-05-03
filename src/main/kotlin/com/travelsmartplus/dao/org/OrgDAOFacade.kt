package com.travelsmartplus.dao.org

import com.travelsmartplus.models.Org

interface OrgDAOFacade {
    suspend fun getOrg(id: Int): Org?
    suspend fun getAllOrgs(): List<Org>
    suspend fun getOrgByDuns(duns: Int): Org?
    suspend fun addOrg(org: Org): Org?
    suspend fun editOrg(id: Int, orgName: String, duns: Int)
    suspend fun deleteOrg(id: Int)
}
