package com.travelsmartplus.models

import com.travelsmartplus.utils.Encryptor
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

@Serializable
data class TravelData(
    val id: Int? = 0,
    val userId: Int,
    val dob: LocalDate,
    val nationality: String,
    val passportNumber: String,
    val passportExpiryDate: LocalDate
)

// Table
object TravelDetails : IntIdTable() {
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val dob = binary("dob")
    val nationality = varchar("nationality", 100)
    val passportNumber = binary("passport_number")
    val passportExpiryDate = binary("passport_expiry")
}

// Entity - Represents row in table
class TravelDataEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TravelDataEntity>(TravelDetails)
    var userId by UserEntity referencedOn TravelDetails.userId
    var dob by TravelDetails.dob
    var nationality by TravelDetails.nationality
    var passportNumber by TravelDetails.passportNumber
    var passportExpiryDate by TravelDetails.passportExpiryDate
}

// Transform entity to data class
fun TravelDataEntity.toTravelData(): TravelData {

    // Decrypt the encrypted fields
    val decryptedDob = Encryptor.decrypt(dob).toLocalDate()
    val decryptedPassportNumber = Encryptor.decrypt(passportNumber)
    val decryptedExpiryDate = Encryptor.decrypt(passportExpiryDate).toLocalDate()
    return TravelData(id.value, userId.id.value, decryptedDob, nationality, decryptedPassportNumber, decryptedExpiryDate)
}


