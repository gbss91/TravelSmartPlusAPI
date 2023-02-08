package com.travelsmartplus.dao.org

import com.travelsmartplus.models.Org

interface OrgDAOFacade {
    suspend fun getOrgByName(orgName: String): Org?
    suspend fun getOrgByDuns(duns: Int): Org?
    suspend fun addOrg(id: Int, orgName: String, duns: Int): Org?
    suspend fun editOrg(id: Int, orgName: String, duns: Int): Boolean
    suspend fun deleteOrg(id: Int, orgName: String, duns: Int): Boolean

}