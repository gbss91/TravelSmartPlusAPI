package com.travelsmartplus.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

//Data class
@Serializable
data class Org(
    val id: Int,
    val orgName: String,
    val duns: Int
)

//Table
object Orgs: IntIdTable() {
    val orgName = varchar("org_name", 50)
    val duns = integer("duns").uniqueIndex()
}

//Entity - Represents row in table
class OrgEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<OrgEntity>(Orgs)
    var orgName by Orgs.orgName
    var duns by Orgs.duns
}