package com.travelsmartplus.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Hotel(
    val hotelChain: String,
    val code: String
)

// Table
object Hotels: IntIdTable() {
    val hotelChain = varchar("hotel_chain", 50)
    val code = varchar("code", 2)
}

// Entity - Represents a row in table
class HotelEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<HotelEntity>(Hotels)
    var hotelChain by Hotels.hotelChain
    var code by Hotels.code
}

// Transform entity to data class
fun HotelEntity.toHotel() = Hotel(hotelChain, code)
