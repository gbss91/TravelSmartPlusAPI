package com.travelsmartplus.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

// Data class
@Serializable
data class User(
    val id: Int? = 0,
    val orgId: Int,
<<<<<<< HEAD
    val firstName: String,
    val lastName: String,
    val email: String,
    val admin: Boolean,
    val password: String,
    val salt: String
=======
    var firstName: String,
    var lastName: String,
    var email: String,
    var admin: Boolean,
    var password: String,
    var salt: String,
    var accountSetup: Boolean,
    var preferredAirlines: List<String>? = null,
    var preferredHotelChains: List<String>? = null,
    var travelData: TravelData? = null
>>>>>>> development
)

// Table
object Users : IntIdTable() {
    val orgId = reference("org_id", Orgs.id, onDelete = ReferenceOption.CASCADE)
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 355).uniqueIndex()
    val admin = bool("admin")
    val password = varchar("password", 355)
    val salt = varchar("salt", 100)
<<<<<<< HEAD
=======
    val accountSetup = bool("account_setup")
    val preferredAirlines = varchar("preferred_airlines", 100).nullable()
    val preferredHotelChains = varchar("preferred_hotels", 100).nullable()
>>>>>>> development
}

// Entity - Represents row in table
class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)
    var orgId by OrgEntity referencedOn Users.orgId
    var firstName by Users.firstName
    var lastName by Users.lastName
    var email by Users.email
    var admin by Users.admin
    var password by Users.password
    var salt by Users.salt
<<<<<<< HEAD
}

// Transform entity to data class
fun UserEntity.toUser() = User(id.value, orgId.id.value, firstName, lastName, email, admin, password, salt)
=======
    var accountSetup by Users.accountSetup
    var preferredAirlines by Users.preferredAirlines
    var preferredHotelChains by Users.preferredHotelChains
}

// Transform entity to data class
fun UserEntity.toUser(): User {
    val preferredAirlinesList = preferredAirlines?.split(", ")
    val preferredHotelChainsList = preferredHotelChains?.split(", ")
    val travelData = TravelDataEntity.find { TravelDetails.userId eq id.value}.singleOrNull()?.toTravelData()
    return User(
        id.value,
        orgId.id.value,
        firstName,
        lastName,
        email,
        admin,
        password,
        salt,
        accountSetup,
        preferredAirlinesList,
        preferredHotelChainsList,
        travelData
    )
}
>>>>>>> development
