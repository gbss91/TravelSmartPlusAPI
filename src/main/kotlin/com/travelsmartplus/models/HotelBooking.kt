package com.travelsmartplus.models

import com.travelsmartplus.utils.BigDecimalSerializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import java.math.BigDecimal

// Data class
@Serializable
data class HotelBooking(
    val id: Int? = 0,
    val hotelName: String,
    val hotelChainCode: String? = null,
    var address: String,
    val checkInDate: LocalDate,
    val checkOutDate: LocalDate,
    val roomType: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val rate: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val totalPrice: BigDecimal,
    val latitude: Double,
    val longitude: Double
)

// Table
object HotelBookings: IntIdTable() {
    val hotelName = varchar("hotel_name", 50)
    val hotelChainCode = varchar("hotel_chain", 5).nullable()
    val address = varchar("address", 100 )
    val checkInDate = date("checkIn_date")
    val checkOutDate = date("checkOut_date")
    val roomType = varchar("room_type", 50).nullable()
    val rate = decimal("rate", 10, 2)
    val totalPrice = decimal("total_price", 10, 2)
    val latitude = double("latitude")
    val longitude = double("longitude")
}

// Entity - Represents row in table
class HotelBookingEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<HotelBookingEntity>(HotelBookings)
    var hotelName by HotelBookings.hotelName
    var hotelChainCode by HotelBookings.hotelChainCode
    var address by HotelBookings.address
    var checkInDate by HotelBookings.checkInDate
    var checkOutDate by HotelBookings.checkOutDate
    var roomType by HotelBookings.roomType
    var rate by HotelBookings.rate
    var totalPrice by HotelBookings.totalPrice
    var latitude by HotelBookings.latitude
    var longitude by HotelBookings.longitude
}

// Transform entity to data class
fun HotelBookingEntity.toHotelBooking(): HotelBooking = HotelBooking(
    id.value, hotelName, hotelChainCode, address, checkInDate.toKotlinLocalDate(), checkOutDate.toKotlinLocalDate(), roomType, rate, totalPrice, latitude, longitude,
)
