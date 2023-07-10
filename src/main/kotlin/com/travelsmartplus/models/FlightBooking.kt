package com.travelsmartplus.models

import com.travelsmartplus.utils.BigDecimalSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import java.math.BigDecimal

// Data class
@Serializable
data class FlightBooking(
    val id: Int? = 0,
    val bookingReference: String,
    val oneWay: Boolean,
    val originCity: String,
    val destinationCity: String,
    val segments: List<FlightSegment>,
    val travelClass: String,
    var status: String,
    @Serializable(with = BigDecimalSerializer::class)
    val totalPrice: BigDecimal,
)

// Table
object FlightBookings: IntIdTable() {
    val bookingReference = varchar("booking_reference", 10)
    val oneWay = bool("one_way")
    val originCity = varchar("origin_city", 50)
    val destinationCity = varchar("destination_city", 50)
    val travelClass = varchar("travel_class", 20)
    val status = varchar("status", 20)
    val totalPrice = decimal("total_price", 10, 2)
}

// Entity - Represents row in table
class FlightBookingEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<FlightBookingEntity>(FlightBookings)
    var bookingReference by FlightBookings.bookingReference
    var oneWay by FlightBookings.oneWay
    var originCity by FlightBookings.originCity
    var destinationCity by FlightBookings.destinationCity
    var travelClass by FlightBookings.travelClass
    var status by FlightBookings.status
    var totalPrice by FlightBookings.totalPrice
}

// Transform entity to data class
fun FlightBookingEntity.toFlightBooking(): FlightBooking{
    val segments = FlightSegmentEntity.find { FlightSegments.flightBookingId eq id }.map { it.toFlightSegment() }
    return FlightBooking(id.value, bookingReference, oneWay, originCity, destinationCity, segments, travelClass, status, totalPrice)
}