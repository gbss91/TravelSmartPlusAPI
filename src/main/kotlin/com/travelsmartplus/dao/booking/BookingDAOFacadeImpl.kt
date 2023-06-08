package com.travelsmartplus.dao.booking

import com.travelsmartplus.models.*
import com.travelsmartplus.utils.DatabaseFactory.dbQuery
import io.ktor.server.plugins.*
import kotlinx.datetime.toJavaLocalDate

/**
 * Implementation of the [BookingDAOFacade] interface.
 * This class provides methods to get, add and delete booking information from the database.
 * @author Gabriel Salas
 */

class BookingDAOFacadeImpl : BookingDAOFacade {
    override suspend fun getBooking(id: Int): Booking? = dbQuery {
        BookingEntity.findById(id)?.toBooking()
    }

    override suspend fun getBookingsByUser(userId: Int): List<Booking> = dbQuery {
        BookingEntity.find { Bookings.userId eq userId }.map { it.toBooking() }
    }

    override suspend fun getAllBookings(orgId: Int): List<Booking> = dbQuery {
        BookingEntity.find { Bookings.orgId eq orgId }.map { it.toBooking() }
    }

    override suspend fun addBooking(booking: Booking): Booking = dbQuery {
        val user = UserEntity.findById(booking.user.id!!) ?: throw NotFoundException("User not found")
        val orgId = OrgEntity[user.orgId.id]
        val flightBooking = FlightBookingEntity[booking.flightBooking.id!!] // All bookings will have a flight booking

        BookingEntity.new {
            userId = user
            this.orgId = orgId
            originIata = booking.origin.iataCode
            destinationIata = booking.destination.iataCode
            departureDate = booking.departureDate.toJavaLocalDate()
            returnDate = booking.returnDate?.toJavaLocalDate()
            flightBookingId = flightBooking
            adultsNumber = booking.adultsNumber
            status = booking.status
            totalPrice = booking.totalPrice
        }.toBooking()
    }

    override suspend fun deleteBooking(id: Int) = dbQuery {
        val booking = BookingEntity.findById(id) ?: throw NotFoundException("Booking not found")
        booking.delete()
    }
}