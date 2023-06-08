package com.travelsmartplus.integration

import com.travelsmartplus.DatabaseTestHelper
import com.travelsmartplus.dao.airport.AirportDAOFacadeImpl
import com.travelsmartplus.dao.flight.FlightBookingDAOFacadeImpl
import com.travelsmartplus.dao.flight.FlightDAOFacadeImpl
import com.travelsmartplus.dao.flight.FlightSegmentDAOFacadeImpl
import com.travelsmartplus.models.Flight
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Before
import org.junit.Test

class FlightIntegrationTests {

    private val flightBookingDao = FlightBookingDAOFacadeImpl()
    private val flightSegmentDao = FlightSegmentDAOFacadeImpl()
    private val flightDao = FlightDAOFacadeImpl()
    private val airportDao = AirportDAOFacadeImpl()

    @Before
    fun setup() {
        DatabaseTestHelper.setup()
    }

    @After
    fun tearDown() {
        DatabaseTestHelper.cleanup()
    }

    @Test
    fun `get flight booking using Id`() = runBlocking {
        val flightBooking = flightBookingDao.getFlightBooking(1)
        assertEquals(1, flightBooking?.id)
    }

    @Test
    fun `get flight using ID`() = runBlocking {
        val flightBooking = flightDao.getFlight(1)
        assertEquals(1, flightBooking?.id)
    }

    @Test
    fun `get flights by segment`() = runBlocking {
        val flights = flightDao.getFlightsBySegment(1)
        assertEquals(12, flights.size)
    }

    @Test
    fun `add flight`() = runBlocking {
        val newFlight = Flight(
            departureAirport = airportDao.getAirport("DUB")!!,
            departureTime = LocalDateTime(2023, 6, 5, 18, 50),
            arrivalAirport = airportDao.getAirport("JFK")!!,
            arrivalTime = LocalDateTime(2023, 6, 6, 5, 7),
            carrierIataCode = "UA"
        )
        val flight = flightDao.addFlight(newFlight, 1)
        assertEquals("DUB", flight.departureAirport.iataCode)
        assertEquals("UA", flight.carrierIataCode)
    }

    @Test
    fun `delete a flight`() = runBlocking {
        flightDao.deleteFlight(2)
        val deletedFlight = flightDao.getFlight(2)
        assertEquals(null, deletedFlight)
    }

    @Test
    fun `delete flight booking should delete associated segments and flights`() = runBlocking {
        flightBookingDao.deleteFlightBooking(1)
        val segment = flightSegmentDao.getFlightSegment(1)
        val flight = flightDao.getFlight(1)
        assertEquals(null, segment)
        assertEquals(null, flight)
    }

}