package com.travelsmartplus.dao.flight

import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.FlightBookingEntity
import com.travelsmartplus.models.toFlightBooking
import com.travelsmartplus.utils.DatabaseFactory.dbQuery
import io.ktor.server.plugins.*

/**
 * Implementation of the [FlightBookingDAOFacade] interface.
 * This class provides methods to get, add and delete flight booking information from the database.
 * @author Gabriel Salas
 */

class FlightBookingDAOFacadeImpl : FlightBookingDAOFacade {
    override suspend fun getFlightBooking(id: Int): FlightBooking? = dbQuery  {
        FlightBookingEntity.findById(id)?.toFlightBooking()
    }

    override suspend fun addFlightBooking(flightBooking: FlightBooking): Int = dbQuery {
        val newFlightBooking = FlightBookingEntity.new {
            bookingReference = flightBooking.bookingReference
            oneWay = flightBooking.oneWay
            originCity = flightBooking.originCity
            destinationCity = flightBooking.destinationCity
            travelClass = flightBooking.travelClass
            status = flightBooking.status
            totalPrice = flightBooking.totalPrice
        }
        newFlightBooking.id.value
    }

    override suspend fun deleteFlightBooking(id: Int) = dbQuery {
        val flightBooking = FlightBookingEntity.findById(id) ?: throw NotFoundException("Flight Booking not found")
        flightBooking.delete()
    }
}