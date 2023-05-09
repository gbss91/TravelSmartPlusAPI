package com.travelsmartplus.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.duration
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

// Data class
@Serializable
data class FlightSegment(
    val id: Int? =0,
    val flights: List<Flight>,
    val direction: String,
    val duration: Duration,
    val stops: Int
)

// Table
object FlightSegments: IntIdTable() {
    val flightBookingId = reference("flight_booking_id", FlightBookings.id, onDelete = ReferenceOption.CASCADE)
    val direction = varchar("direction", 20)
    val duration = duration("duration")
    val stops = integer("stops")
}

// Entity - Represents row in table
class FlightSegmentEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<FlightSegmentEntity>(FlightSegments)
    var flightBookingId by FlightBookingEntity referencedOn FlightSegments.flightBookingId
    var direction by FlightSegments.direction
    var duration by FlightSegments.duration
    var stops by FlightSegments.stops
}

// Transform entity to data class
fun FlightSegmentEntity.toFlightSegment(): FlightSegment {
    val flights = FlightEntity.find {Flights.flightSegmentId eq id}.map { it.toFlight() }
    return FlightSegment(id.value, flights, direction, duration.toKotlinDuration(), stops)
}