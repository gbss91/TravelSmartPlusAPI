package com.travelsmartplus.services

import com.travelsmartplus.dao.booking.BookingDAOFacadeImpl
import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.HotelBooking
import com.travelsmartplus.models.User
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.recomendation.KNNRecommendationFacadeImpl

class BookingServiceFacadeImpl : BookingServiceFacade {
    private val flightService: FlightBookingServiceFacadeImpl = FlightBookingServiceFacadeImpl()
    private val hotelService: HotelBookingServiceFacadeImpl = HotelBookingServiceFacadeImpl()
    private val knn: KNNRecommendationFacadeImpl = KNNRecommendationFacadeImpl()

    override suspend fun new(bookingSearchRequest: BookingSearchRequest, previousBookings: List<Booking>, user: User): Booking? {

        try {
            // Train algorithm
            knn.trainModel(previousBookings, 3)

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
            val hotelResults: List<HotelBooking>
            val predictedHotel: HotelBooking?
            if (bookingSearchRequest.hotel) {
                hotelResults = hotelService.getHotels(bookingSearchRequest)
                predictedHotel = knn.predict(hotelResults) ?: return null

                val nights = bookingSearchRequest.checkOutDate!!.dayOfYear - bookingSearchRequest.checkInDate!!.dayOfYear
                val totalPrice = predictedHotel.rate * nights.toBigDecimal()
                newBooking.hotelBooking = predictedHotel
                newBooking.totalPrice = predictedFlight.totalPrice + totalPrice
            }

            return newBooking

        } catch (e: IllegalStateException) {
            e.printStackTrace()
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}