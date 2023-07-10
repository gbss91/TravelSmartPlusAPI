package com.travelsmartplus.dao.hotel

import com.travelsmartplus.models.HotelBooking
import com.travelsmartplus.models.HotelBookingEntity
import com.travelsmartplus.models.toHotelBooking
import com.travelsmartplus.utils.DatabaseFactory.dbQuery
import io.ktor.server.plugins.*
import kotlinx.datetime.toJavaLocalDate

/**
 * Implementation of the [HotelBookingDAOFacade] interface.
 * This class provides methods to get, add and delete hotel booking information from the database.
 * @author Gabriel Salas
 */

class HotelBookingDAOFacadeImpl : HotelBookingDAOFacade {
    override suspend fun getHotelBooking(id: Int): HotelBooking? = dbQuery {
        HotelBookingEntity.findById(id)?.toHotelBooking()
    }

    override suspend fun addHotelBooking(hotelBooking: HotelBooking): Int = dbQuery {
        val newHotelBooking = HotelBookingEntity.new {
            hotelName = hotelBooking.hotelName
            hotelChainCode = hotelChainCode
            address = hotelBooking.address
            checkInDate = hotelBooking.checkInDate.toJavaLocalDate()
            checkOutDate = hotelBooking.checkOutDate.toJavaLocalDate()
            rate = hotelBooking.rate
            totalPrice = hotelBooking.totalPrice
            latitude = hotelBooking.latitude
            longitude = hotelBooking.longitude
        }
        newHotelBooking.id.value
    }

    override suspend fun deleteHotelBooking(id: Int) = dbQuery {
        val hotelBooking = HotelBookingEntity.findById(id) ?: throw NotFoundException("Hotel Booking not found")
        hotelBooking.delete()
    }
}