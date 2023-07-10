package com.travelsmartplus.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Airline(
    val id: Int,
    val airlineName: String,
    val iataCode: String,
    val icaoCode: String
)

// Table
object Airlines: IntIdTable() {
    val airlineName = varchar("airline_name", 50)
    val iataCode = varchar("iata_code", 3)
    val icaoCode = varchar("icao_code", 4).uniqueIndex()
}

// Entity - Represents a row in table
class AirlineEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<AirlineEntity>(Airlines)
    var airlineName by Airlines.airlineName
    var iataCode by Airlines.iataCode
    var icaoCode by Airlines.icaoCode
}

// Transform entity to data class
fun AirlineEntity.toAirline() = Airline(id.value, airlineName, iataCode, icaoCode)