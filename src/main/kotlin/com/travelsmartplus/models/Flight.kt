package com.travelsmartplus.models

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime



// Data class
@Serializable
data class Flight(
    val id: Int? = 0,
    val departureAirport: Airport,
    val departureTime: LocalDateTime,
    val arrivalAirport: Airport,
    val arrivalTime: LocalDateTime,
    val carrierIataCode: String
)

// Table
object Flights: IntIdTable() {
    val flightSegmentId = reference("flight_segment_id", FlightSegments.id, onDelete = ReferenceOption.CASCADE)
    val departureIataCode = varchar("departure_iata_code", 3)
    val departureTime = datetime("departure_time")
    val arrivalIataCode = varchar("arrival_iata_code", 3)
    val arrivalTime = datetime("arrival_time")
    val carrierIataCode = varchar("carrier_iata_code", 3)
}

// Entity - Represents row in table
class FlightEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<FlightEntity>(Flights)
    var flightSegmentId by FlightSegmentEntity referencedOn Flights.flightSegmentId
    var departureIataCode by Flights.departureIataCode
    var departureTime by Flights.departureTime
    var arrivalIataCode by Flights.arrivalIataCode
    var arrivalTime by Flights.arrivalTime
    var carrierIataCode by Flights.carrierIataCode
}

// Transform entity to data class
fun FlightEntity.toFlight(): Flight {
    val departureAirport = AirportEntity.find { Airports.iataCode eq departureIataCode }.single().toAirport()
    val arrivalAirport = AirportEntity.find { Airports.iataCode eq arrivalIataCode }.single().toAirport()

    return Flight(id.value, departureAirport, departureTime.toKotlinLocalDateTime(), arrivalAirport, arrivalTime.toKotlinLocalDateTime(), carrierIataCode)

}