package com.travelsmartplus.services

import com.travelsmartplus.dao.booking.BookingDAOFacadeImpl
import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.recomendation.KNNRecommendationFacadeImpl

/**
 * Implementation of the [BookingServiceFacade] interface that provides booking services.
 * Utilises the K-nearest neighbors (KNN) algorithm to train the algorithm and make flight and hotel predictions
 * based on previous bookings and the user's preferred hotels and airlines.
 *
 * @property flightService An instance of the [FlightBookingServiceFacadeImpl] used to retrieve flight booking information from Amadeus API.
 * @property hotelService An instance of the [HotelBookingServiceFacadeImpl] used to retrieve hotel booking information from Amadeus API.
 * @property knn An instance of the [KNNRecommendationFacadeImpl] that implements the KNN algorithm for recommendation.
 * @return A predicted booking or null if less than 2 previous bookings available.
 * @throws IllegalStateException if there is an error training algorithm or predicting booking.
 *
 * @author Gabriel Salas
 */

class BookingServiceFacadeImpl : BookingServiceFacade {
    private val flightService: FlightBookingServiceFacadeImpl = FlightBookingServiceFacadeImpl()
    private val hotelService: HotelBookingServiceFacadeImpl = HotelBookingServiceFacadeImpl()
    private val knn: KNNRecommendationFacadeImpl = KNNRecommendationFacadeImpl()
    private val bookingDAO: BookingDAOFacadeImpl = BookingDAOFacadeImpl()

    override suspend fun newPredictedBooking(bookingSearchRequest: BookingSearchRequest, user: User): Booking? {

        try {
            val userId = user.id
            if (userId == null || userId == 0) return null

            val previousBookings = bookingDAO.getAllBookings(user.id)
            val preferredAirlines = user.preferredAirlines ?: emptySet()
            val preferredHotels = user.preferredHotelChains ?: emptySet()

            // Return null if previous bookings less than 2, else create predicted booking
            if (previousBookings.size < 2) {
                return null
            } else {

                // Train algorithm
                knn.trainModel(previousBookings, preferredAirlines, preferredHotels)

                // Search and predict flight
                val flightResults = flightService.getFlights(bookingSearchRequest)
                val predictedFlight = knn.predict(flightResults) ?: return null

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
                    val predictedHotel = knn.predict(hotelResults) ?: return null

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
}