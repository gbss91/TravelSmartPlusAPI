package com.travelsmartplus.services

import com.travelsmartplus.dao.booking.BookingDAOFacadeImpl
import com.travelsmartplus.dao.flight.FlightBookingDAOFacadeImpl
import com.travelsmartplus.dao.flight.FlightDAOFacadeImpl
import com.travelsmartplus.dao.flight.FlightSegmentDAOFacadeImpl
import com.travelsmartplus.dao.hotel.HotelBookingDAOFacadeImpl
import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.recomendation.ContentBasedRecommendationFacadeImpl
import com.travelsmartplus.recomendation.KNNRecommendationFacadeImpl

/**
 * Implementation of the [BookingServiceFacade] interface that provides booking services.
 * Utilises the K-nearest neighbors (KNN) and Content-Based algorithms to make predictions and provides
 * a function to add all the booking sections to database.
 *
 * @property flightService An instance of the [FlightBookingServiceFacadeImpl] used to retrieve flight booking information from Amadeus API.
 * @property hotelService An instance of the [HotelBookingServiceFacadeImpl] used to retrieve hotel booking information from Amadeus API.
 * @property knn An instance of the [KNNRecommendationFacadeImpl] that implements the KNN algorithm for recommendation.
 * @property contentBased An instance of [ContentBasedRecommendationFacadeImpl] that recommends bookings based on preferences.
 * @return A predicted booking or null if less than 2 previous bookings available.
 * @throws IllegalStateException if there is an error training algorithm or predicting booking.
 *
 * @author Gabriel Salas
 */

class BookingServiceFacadeImpl : BookingServiceFacade {
    private val flightService: FlightBookingServiceFacadeImpl = FlightBookingServiceFacadeImpl()
    private val hotelService: HotelBookingServiceFacadeImpl = HotelBookingServiceFacadeImpl()
    private val placesService: PlacesServiceFacadeImpl = PlacesServiceFacadeImpl()
    private val knn: KNNRecommendationFacadeImpl = KNNRecommendationFacadeImpl()
    private val contentBased: ContentBasedRecommendationFacadeImpl = ContentBasedRecommendationFacadeImpl()
    private val bookingDAO: BookingDAOFacadeImpl = BookingDAOFacadeImpl()
    private val flightDAO: FlightDAOFacadeImpl = FlightDAOFacadeImpl()
    private val flightSegmentDAO: FlightSegmentDAOFacadeImpl = FlightSegmentDAOFacadeImpl()
    private val flightBookingDAO: FlightBookingDAOFacadeImpl = FlightBookingDAOFacadeImpl()
    private val hotelBookingDAO: HotelBookingDAOFacadeImpl = HotelBookingDAOFacadeImpl()

    override suspend fun newPredictedBooking(bookingSearchRequest: BookingSearchRequest, user: User): Booking? {

        try {
            val userId = user.id
            if (userId == null || userId == 0) return null

            val previousBookings = bookingDAO.getBookingsByUser(user.id)
            val preferredAirlines = user.preferredAirlines ?: emptyList()
            val preferredHotels = user.preferredHotelChains ?: emptyList()
            val flightResults = flightService.getFlights(bookingSearchRequest)

            if (flightResults.isEmpty()) return null // Return null if no flights found

            // Use Content Based recommendation if less than 2 bookings - else use KNN Algorithm
            if (previousBookings.size < 2) {

                // Recommend Flight or first flight
                val predictedFlight = contentBased.recommendFlights(preferredAirlines, flightResults) ?: flightResults[0]

                val newBooking = Booking(
                    user = user,
                    origin = bookingSearchRequest.origin,
                    destination = bookingSearchRequest.destination,
                    departureDate = bookingSearchRequest.departureDate,
                    returnDate = bookingSearchRequest.returnDate,
                    flightBooking = predictedFlight,
                    hotelBooking = null,
                    adultsNumber = bookingSearchRequest.adultsNumber,
                    status = "PENDING",
                    totalPrice = predictedFlight.totalPrice
                )

                // Search and predict hotel if included in request
                if (bookingSearchRequest.hotel) {
                    val hotelResults = hotelService.getHotels(bookingSearchRequest)
                    val updatedHotelResults = placesService.getAddress(hotelResults)
                    if (updatedHotelResults.isEmpty()) return null // Return null if no hotels found

                    val predictedHotel = contentBased.recommendHotels(preferredHotels, updatedHotelResults) ?: updatedHotelResults[0]

                    val nights = bookingSearchRequest.checkOutDate!!.dayOfYear - bookingSearchRequest.checkInDate!!.dayOfYear
                    val totalPrice = predictedHotel.rate * nights.toBigDecimal()
                    newBooking.hotelBooking = predictedHotel
                    newBooking.totalPrice += totalPrice
                }

                return newBooking

            } else {

                // Train algorithm
                knn.trainModel(previousBookings, preferredAirlines, preferredHotels)

                // Predict flight
                val predictedFlight = knn.predict(flightResults) ?: flightResults[0]

                val newBooking = Booking(
                    user = user,
                    origin = bookingSearchRequest.origin,
                    destination = bookingSearchRequest.destination,
                    departureDate = bookingSearchRequest.departureDate,
                    returnDate = bookingSearchRequest.returnDate,
                    flightBooking = predictedFlight,
                    hotelBooking = null,
                    adultsNumber = bookingSearchRequest.adultsNumber,
                    status = "PENDING",
                    totalPrice = predictedFlight.totalPrice
                )

                // Search and predict hotel if included in request
                if (bookingSearchRequest.hotel) {
                    val hotelResults = hotelService.getHotels(bookingSearchRequest)
                    val predictedHotel = knn.predict(hotelResults) ?: hotelResults[0]

                    val nights = bookingSearchRequest.checkOutDate!!.dayOfYear - bookingSearchRequest.checkInDate!!.dayOfYear
                    val totalPrice = predictedHotel.rate * nights.toBigDecimal()
                    newBooking.hotelBooking = predictedHotel
                    newBooking.totalPrice += totalPrice
                }
                return newBooking
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            throw IllegalStateException("Failed to predict booking.")
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Failed to predict booking.")
        }
    }

    override suspend fun addBooking(booking: Booking): Booking {
        try {
            // Add booking elements in correct order
            // Add Flight Booking
            val flightBookingId = flightBookingDAO.addFlightBooking(booking.flightBooking)

            // Add Flight segments
            val flightSegmentIds = mutableListOf<Int>()
            for (segment in booking.flightBooking.segments) {
                val flightSegmentId = flightSegmentDAO.addFlightSegment(segment, flightBookingId)
                flightSegmentIds.add(flightSegmentId)
            }

            // Add Flights
            for ((index, flightSegmentId) in flightSegmentIds.withIndex()) {
                val flightSegment = booking.flightBooking.segments[index]
                for (flight in flightSegment.flights) {
                    flightDAO.addFlight(flight, flightSegmentId)
                }
            }

            // Add Hotel Booking if needed
            val hotelBooking = booking.hotelBooking
            val hotelBookingId = if (hotelBooking != null) hotelBookingDAO.addHotelBooking(hotelBooking) else null

            // Add Booking
            return bookingDAO.addBooking(booking, flightBookingId, hotelBookingId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Failed to save booking.")
        }
    }
}