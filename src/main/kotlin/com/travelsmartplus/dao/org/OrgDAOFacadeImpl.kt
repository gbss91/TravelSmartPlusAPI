package com.travelsmartplus.dao.org

import com.travelsmartplus.models.Org
import com.travelsmartplus.models.OrgEntity
import com.travelsmartplus.models.Orgs
import com.travelsmartplus.models.toOrg
import com.travelsmartplus.utils.DatabaseFactory.dbQuery
import io.ktor.server.plugins.*

/**
 * Implementation of the [OrgDAOFacade] interface.
 * This class provides methods to get, add and delete org information from the database.
 * @author Gabriel Salas
 */

class OrgDAOFacadeImpl : OrgDAOFacade {

    override suspend fun getOrg(id: Int): Org? = dbQuery {
        OrgEntity.findById(id)?.toOrg()
    }

    override suspend fun getAllOrgs(): List<Org> = dbQuery {
        OrgEntity.all().map(OrgEntity::toOrg)
    }

    override suspend fun getOrgByDuns(duns: Int): Org? = dbQuery {
        OrgEntity.find { Orgs.duns eq duns }.map(OrgEntity::toOrg).singleOrNull()
    }

    override suspend fun addOrg(org: Org): Org? = dbQuery {
        if (OrgEntity.find { Orgs.duns eq org.duns }.firstOrNull() != null) {
            null
        } else {
            OrgEntity.new {
                this.orgName = org.orgName
                this.duns = org.duns
            }.toOrg()
        }
    }

    override suspend fun editOrg(id: Int, orgName: String, duns: Int) = dbQuery {
        val org = OrgEntity.findById(id) ?: throw NotFoundException("Organization not found")
        OrgEntity[org.id].orgName = orgName
        OrgEntity[org.id].duns = duns
    }

    override suspend fun deleteOrg(id: Int) = dbQuery {
        val org = OrgEntity.findById(id) ?: throw NotFoundException("Organization not found")
        org.delete()
    }
}