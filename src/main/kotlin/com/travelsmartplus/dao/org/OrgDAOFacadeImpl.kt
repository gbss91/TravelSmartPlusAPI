package com.travelsmartplus.dao.org

import com.travelsmartplus.dao.DatabaseFactory.dbQuery
import com.travelsmartplus.models.*

class OrgDAOFacadeImpl : OrgDAOFacade {

    override suspend fun getOrg(id: Int): Org? = dbQuery {
        OrgEntity.findById(id)?.toOrg()
    }

    override suspend fun getAllOrgs(): List<Org> = dbQuery {
        OrgEntity.all().map(OrgEntity::toOrg)
    }

    override suspend fun getOrgByName(orgName: String): Org? = dbQuery {
        OrgEntity.find { Orgs.orgName eq orgName}.map(OrgEntity::toOrg).singleOrNull()
    }

    override suspend fun getOrgByDuns(duns: Int): Org? = dbQuery {
        OrgEntity.find { Orgs.duns eq duns}.map(OrgEntity::toOrg).singleOrNull()
    }

    override suspend fun addOrg(org: Org): Org? = dbQuery {
        OrgEntity.new {
            this.orgName = org.orgName
            this.duns = org.duns
        }.toOrg()
    }

    override suspend fun editOrg(id: Int, orgName: String, duns: Int) = dbQuery {
        OrgEntity[id].orgName = orgName
        OrgEntity[id].duns= duns
    }

    override suspend fun deleteOrg(id: Int) = dbQuery {
        OrgEntity[id].delete()
    }
}