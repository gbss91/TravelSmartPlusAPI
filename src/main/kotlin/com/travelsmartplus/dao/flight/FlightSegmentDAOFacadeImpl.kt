package com.travelsmartplus.dao.flight

import com.travelsmartplus.models.*
import com.travelsmartplus.utils.DatabaseFactory.dbQuery
import io.ktor.server.plugins.*
import kotlin.time.toJavaDuration

/**
 * Implementation of the [FlightSegmentDAOFacade] interface.
 * This class provides methods to get, add and delete flight segments information from the database.
 * @author Gabriel Salas
 */

class FlightSegmentDAOFacadeImpl : FlightSegmentDAOFacade {
    override suspend fun getFlightSegment(id: Int): FlightSegment? = dbQuery {
        FlightSegmentEntity.findById(id)?.toFlightSegment()
    }

    override suspend fun getFlightSegmentByBooking(bookingId: Int): List<FlightSegment> = dbQuery {
        FlightSegmentEntity.find { FlightSegments.flightBookingId eq bookingId }.map { it.toFlightSegment() }
    }

    override suspend fun addFlightSegment(flightSegment: FlightSegment, flightBookingId: Int): Int = dbQuery {
        val flightBooking = FlightBookingEntity[flightBookingId]
        val newFlightSegment = FlightSegmentEntity.new {
            this.flightBookingId = flightBooking
            direction = flightSegment.direction
            duration = flightSegment.duration.toJavaDuration()
            stops = flightSegment.stops
        }
        newFlightSegment.id.value
    }

    override suspend fun deleteFlightSegment(id: Int) = dbQuery {
        val flightSegment = FlightSegmentEntity.findById(id) ?: throw NotFoundException("Segment not found")
        flightSegment.delete()
    }
}