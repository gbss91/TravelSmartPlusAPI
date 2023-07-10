package com.travelsmartplus.services

import com.travelsmartplus.apis.AmadeusAuthenticationApi
import com.travelsmartplus.apis.FlightApi
import com.travelsmartplus.apis.apiResponses.AmadeusFlightOffersResponse
import com.travelsmartplus.dao.airline.AirlineDAOFacadeImpl
import com.travelsmartplus.dao.airport.AirportDAOFacadeImpl
import com.travelsmartplus.models.Flight
import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.FlightSegment
import com.travelsmartplus.models.requests.BookingSearchRequest
import io.ktor.server.plugins.*
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

/**
 * Implementation of the [FlightBookingServiceFacade] interface. Handles data processing logic for API responses.
 * @author Gabriel Salas
 */

class FlightBookingServiceFacadeImpl : FlightBookingServiceFacade {
    private val flightApi: FlightApi = FlightApi()
    private val airportDAO: AirportDAOFacadeImpl = AirportDAOFacadeImpl()
    private val airlineDAO: AirlineDAOFacadeImpl = AirlineDAOFacadeImpl()

    override suspend fun getFlights(bookingSearchRequest: BookingSearchRequest): List<FlightBooking> {
        try {
            // Get token
            val token = AmadeusAuthenticationApi().call()

            // Get Flight Data and parse it into Flight Bookings
            val flightOffersResponse = flightApi.getFlights(token, bookingSearchRequest)
            return createFlightBookingsFromResponse(flightOffersResponse, bookingSearchRequest)

        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception()
        }
    }

    private suspend fun createFlightBookingsFromResponse(
        response: AmadeusFlightOffersResponse,
        bookingSearchRequest: BookingSearchRequest
    ): List<FlightBooking> {
        return response.data.map { flightOffer ->
            val segments = flightOffer.itineraries.mapIndexed { index, itinerary ->
                val direction = if (index == 0) "Outbound" else "Inbound"
                val flights = itinerary.segments.map { flight ->

                    // Create flights
                    Flight(
                        departureAirport = airportDAO.getAirport(flight.departure.iataCode) ?: throw NotFoundException(),
                        departureTime = flight.departure.at.toLocalDateTime(),
                        arrivalAirport = airportDAO.getAirport(flight.arrival.iataCode) ?: throw NotFoundException(),
                        arrivalTime = flight.arrival.at.toLocalDateTime(),
                        carrierIataCode = flight.carrierCode,
                        carrierName = airlineDAO.getAirline(flight.carrierCode)?.airlineName ?: flight.carrierCode // Display Iata if no name
                    )
                }

                // Create flight segments
                FlightSegment(
                    flights = flights,
                    direction = direction,
                    duration = Duration.parse(itinerary.duration),
                    stops = flights.size - 1
                )
            }

            // Create flight bookings
            FlightBooking(
                bookingReference = generateRandomBookingCode(),
                oneWay = bookingSearchRequest.oneWay,
                originCity = bookingSearchRequest.origin.city,
                destinationCity = bookingSearchRequest.destination.city,
                segments = segments,
                travelClass = bookingSearchRequest.travelClass,
                status = "OFFER",
                totalPrice = flightOffer.price.grandTotal.toBigDecimal()
            )
        }
    }

    private fun generateRandomBookingCode(): String {
        val characters = ('A'..'Z') + ('0'..'9')
        val codeLength = 6
        val random = java.util.Random()

        return (1..codeLength)
            .map { characters[random.nextInt(characters.size)] }
            .joinToString("")
    }
}