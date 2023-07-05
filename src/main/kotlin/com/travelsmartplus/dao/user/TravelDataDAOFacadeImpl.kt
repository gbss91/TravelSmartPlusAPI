package com.travelsmartplus.dao.user

import com.travelsmartplus.models.*
import com.travelsmartplus.utils.DatabaseFactory.dbQuery
import com.travelsmartplus.utils.Encryptor
import io.ktor.server.plugins.*

/**
 * Implementation of the [TravelDataDAOFacade] interface.
 * This class provides methods to get, add, edit and delete  travel information for a user.
 * This is required to proceed a booking with Amadeus and all sensitive data will be encrypted in database.
 * @author Gabriel Salas
 */

class TravelDataDAOFacadeImpl : TravelDataDAOFacade {

    override suspend fun getTravelData(userId: Int): TravelData? = dbQuery {
        TravelDataEntity.find { TravelDetails.userId eq userId }.map(TravelDataEntity::toTravelData).singleOrNull()
    }

    override suspend fun addTravelData(travelData: TravelData): TravelData? = dbQuery {
        val user = UserEntity[travelData.userId]

        if (TravelDataEntity.find { TravelDetails.userId eq travelData.userId }.firstOrNull() != null) {
            null
        } else {
            // Encrypt sensitive data
            val encryptedPassportNumber = Encryptor.encrypt(travelData.passportNumber)
            val encryptedDob = Encryptor.encrypt(travelData.dob.toString())
            val encryptedExpiryDate = Encryptor.encrypt(travelData.passportExpiryDate.toString())

            // Save encrypted data to the database
            TravelDataEntity.new {
                this.userId = user
                this.dob = encryptedDob
                this.nationality = travelData.nationality
                this.passportNumber = encryptedPassportNumber
                this.passportExpiryDate = encryptedExpiryDate
            }.toTravelData()
        }
    }

    override suspend fun editTravelData(id: Int, travelData: TravelData): TravelData = dbQuery {
        val travelEntity = TravelDataEntity.findById(id) ?: throw NotFoundException("Travel Data not found")

        // Encrypt sensitive data
        val encryptedPassportNumber = Encryptor.encrypt(travelData.passportNumber)
        val encryptedDob = Encryptor.encrypt(travelData.dob.toString())
        val encryptedExpiryDate = Encryptor.encrypt(travelData.passportExpiryDate.toString())

        travelEntity.apply {
            dob = encryptedDob
            nationality = travelData.nationality
            passportNumber = encryptedPassportNumber
            passportExpiryDate = encryptedExpiryDate
        }
        travelEntity.toTravelData()

    }

    override suspend fun deleteTravelData(id: Int) = dbQuery {
        val travelData = TravelDataEntity.findById(id) ?: throw NotFoundException("Travel data not found")
        travelData.delete()
    }
}