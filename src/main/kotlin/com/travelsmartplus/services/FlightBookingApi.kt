package com.travelsmartplus.services

import com.travelsmartplus.dao.airport.AirportDAOFacadeImpl
import com.travelsmartplus.models.Flight
import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.FlightSegment
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.services.Endpoints.SEARCH_FLIGHT
import com.travelsmartplus.services.apiResponses.AmadeusFlightOffersResponse
import com.travelsmartplus.utils.HttpClientFactory
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.plugins.*
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.time.Duration

/**
 * Retrieves flights from the Amadeus API based on the provided search parameters.
 * @
 * @param token The authorization token for accessing the Amadeus API.
 * @param bookingSearchRequest The search parameters for flight bookings.
 * @return The flight offers retrieved from the API.
 * @throws IllegalStateException if there is an error retrieving flights or the API response indicates failure.
 */

class FlightBookingApi {
    private val client = HttpClientFactory.createHttpClient()

    suspend fun call(bookingSearchRequest: BookingSearchRequest): List<FlightBooking> {

        // Get token
        val token = AmadeusAuthentication().call()

        // Get Flight Data and parse it into Flight Bookings
        val flightData = getFlights(token, bookingSearchRequest)
        return createFlightBookingsFromResponse(flightData, bookingSearchRequest)
    }

    private suspend fun getFlights(token: String, bookingSearchRequest: BookingSearchRequest): AmadeusFlightOffersResponse {
        try {
            val response = client.get(SEARCH_FLIGHT) {
                url {
                    parameters.append("originLocationCode", bookingSearchRequest.origin.iataCode)
                    parameters.append("destinationLocationCode", bookingSearchRequest.destination.iataCode)
                    parameters.append("departureDate", bookingSearchRequest.departureDate.toString())
                    if (!bookingSearchRequest.oneWay) {
                        parameters.append("returnDate", bookingSearchRequest.returnDate.toString()) // Only for return bookings
                    }
                    parameters.append("adults", bookingSearchRequest.adultsNumber.toString())
                    parameters.append("travelClass", bookingSearchRequest.travelClass)
                    parameters.append("currencyCode", "EUR")
                    parameters.append("maxPrice", 100.toString())
                    parameters.append("max", 20.toString())
                }
                headers {
                    append(HttpHeaders.Authorization, token)
                }
            }

            if (response.status.isSuccess()) {
                return Json.decodeFromString<AmadeusFlightOffersResponse>(response.body())
            } else {
                throw IllegalStateException("Failed to retrieve flights. Response status: ${response.status}")
            }
        } catch (e: Exception) {
            throw IllegalStateException("Error occurred while retrieving flights", e)
        } finally {
            client.close()
        }
    }

    private suspend fun createFlightBookingsFromResponse(response: AmadeusFlightOffersResponse, bookingSearchRequest: BookingSearchRequest): List<FlightBooking> {
        return response.data.map { flightOffer ->
            val segments = flightOffer.itineraries.mapIndexed { index, itinerary ->
                val direction = if (index == 0) "Outbound" else "Inbound"
                val flights = itinerary.segments.map { flight ->

                    // Create flights
                    Flight(
                        departureAirport = AirportDAOFacadeImpl().getAirport(flight.departure.iataCode) ?: throw NotFoundException(),
                        departureTime = flight.departure.at.toLocalDateTime(),
                        arrivalAirport = AirportDAOFacadeImpl().getAirport(flight.arrival.iataCode) ?: throw NotFoundException(),
                        arrivalTime = flight.arrival.at.toLocalDateTime(),
                        carrierIataCode = flight.carrierCode
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
                bookingReference = "RGETHE",
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
}