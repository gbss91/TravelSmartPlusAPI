package com.travelsmartplus.dao.flight

import com.travelsmartplus.dao.DatabaseFactory.dbQuery
import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.FlightBookingEntity
import com.travelsmartplus.models.toFlightBooking
import io.ktor.server.plugins.*

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