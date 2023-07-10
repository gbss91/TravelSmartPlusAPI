package com.travelsmartplus.models

import com.travelsmartplus.utils.BigDecimalSerializer
import io.ktor.server.plugins.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.date
import java.math.BigDecimal


// Data class
@Serializable
data class Booking(
    val id: Int? = 0,
    val user: User,
    val origin: Airport,
    val destination: Airport,
    val departureDate: LocalDate,
    val returnDate: LocalDate?,
    val flightBooking: FlightBooking,
    var hotelBooking: HotelBooking?,
    val adultsNumber: Int,
    var status: String,
    @Serializable(with = BigDecimalSerializer::class)
    var totalPrice: BigDecimal,
    var imageUrl: String? = null
)

// Table
object Bookings : IntIdTable() {
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
    val orgId = reference("org_id", Orgs.id, onDelete = ReferenceOption.CASCADE)
    val originIata = varchar("origin_city", 3)
    val destinationIata = varchar("destination_city", 3)
    val departureDate = date("departure_date")
    val returnDate = date("return_date").nullable()
    val flightBookingId = reference("flight_booking_id", FlightBookings.id, onDelete = ReferenceOption.CASCADE)
    val hotelBookingId = reference("hotel_booking_id", HotelBookings.id, onDelete = ReferenceOption.CASCADE).nullable()
    val adultsNumber = integer("adults_no")
    val status = varchar("status", 20)
    val totalPrice = decimal("total_price", 10, 2)
}

// Entity - Represents row in table
class BookingEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BookingEntity>(Bookings)

    var userId by UserEntity referencedOn Bookings.userId
    var orgId by OrgEntity referencedOn Bookings.orgId
    var originIata by Bookings.originIata
    var destinationIata by Bookings.destinationIata
    var departureDate by Bookings.departureDate
    var returnDate by Bookings.returnDate
    var flightBookingId by FlightBookingEntity referencedOn Bookings.flightBookingId
    var hotelBookingId by HotelBookingEntity optionalReferencedOn Bookings.hotelBookingId
    var adultsNumber by Bookings.adultsNumber
    var status by Bookings.status
    var totalPrice by Bookings.totalPrice
}

// Transform entity to data class
fun BookingEntity.toBooking(): Booking {
    val user = UserEntity.findById(userId.id)?.toUser() ?: throw NotFoundException("User not found")
    val origin = AirportEntity.find { Airports.iataCode eq originIata }.single().toAirport()
    val destination = AirportEntity.find { Airports.iataCode eq destinationIata }.single().toAirport()
    val flightBooking = FlightBookingEntity.findById(flightBookingId.id)!!.toFlightBooking()
    val hotelBooking = hotelBookingId?.let { HotelBookingEntity.findById(it.id)?.toHotelBooking() }

    return Booking(
        id.value,
        user,
        origin,
        destination,
        departureDate.toKotlinLocalDate(),
        returnDate?.toKotlinLocalDate(),
        flightBooking,
        hotelBooking,
        adultsNumber,
        status,
        totalPrice
    )

}