package com.travelsmartplus.dao.org

import com.travelsmartplus.models.Org

class OrgDAOFacadeImpl : OrgDAOFacade {
    override suspend fun getOrgByName(orgName: String): Org? {
        TODO("Not yet implemented")
    }

    override suspend fun getOrgByDuns(duns: Int): Org? {
        TODO("Not yet implemented")
    }

    override suspend fun addOrg(id: Int, orgName: String, duns: Int): Org? {
        TODO("Not yet implemented")
    }

    override suspend fun editOrg(id: Int, orgName: String, duns: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteOrg(id: Int, orgName: String, duns: Int): Boolean {
        TODO("Not yet implemented")
    }
}