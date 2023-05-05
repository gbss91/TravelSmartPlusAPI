package com.travelsmartplus.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Airport(
    val id: Int,
    val airportName: String,
    val city: String,
    val country: String,
    val iataCode: String,
    val icaoCode: String,
    val latitude: Double,
    val longitude: Double
)

// Table
object Airports: IntIdTable() {
    val airportName = varchar("airport_name", 355)
    val city = varchar("city", 50)
    val country = varchar("country", 50)
    val iataCode = varchar("iata_code", 3).uniqueIndex()
    val icaoCode = varchar("icao_code", 4).uniqueIndex()
    val latitude = double("latitude")
    val longitude = double("longitude")
}

// Entity - Represents a row in the table
class AirportEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<AirportEntity>(Airports)
    var airportName by Airports.airportName
    var city by Airports.city
    var country by Airports.country
    var iataCode by Airports.iataCode
    var icaoCode by Airports.icaoCode
    var latitude by Airports.latitude
    var longitude by Airports.longitude
}

// Transform entity to data class
fun AirportEntity.toAirport() = Airport(id.value, airportName, city, country, iataCode, icaoCode, latitude, longitude)


