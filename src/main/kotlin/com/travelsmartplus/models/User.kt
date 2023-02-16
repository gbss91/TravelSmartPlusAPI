package com.travelsmartplus.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

//Data class
@Serializable
data class User(
    val id: Int? = 0,
    val orgId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val salt: String
)

//Table
object Users: IntIdTable() {
    val orgId = reference("org_id", Orgs.id, onDelete = ReferenceOption.CASCADE)
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 355).uniqueIndex()
    val password = varchar("password", 355)
    val salt = varchar("salt", 50)
}

//Entity - Represents row in table
class UserEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)
    var orgId by OrgEntity referencedOn Users.orgId
    var firstName by Users.firstName
    var lastName by Users.lastName
    var email by Users.email
    var password by Users.password
    var salt by Users.salt

}

//Transform entity to data class
fun UserEntity.toUser() = User(id.value, orgId.id.value, firstName, lastName, email, password, salt)

