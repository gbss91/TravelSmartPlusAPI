package com.travelsmartplus.models.requests

<<<<<<< HEAD
data class BookingSearchRequest()
=======
import com.travelsmartplus.models.Airport
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * Represents a booking search request made by the user.
 * @author Gabriel Salas
 * @property userId Indicates user making the request. Required for POST calls where there is not query or url params.
 * @property oneWay Indicates if the search is for a one-way trip.
 * @property nonStop Indicates if search is for non-stop flights only.
 * @property origin The origin [Airport] for the booking search.
 * @property destination The destination [Airport] for the booking search.
 * @property departureDate The departure date for the booking search.
 * @property returnDate The return date for the booking search (null for one-way trips).
 * @property adultsNumber The number of adults for the booking search.
 * @property travelClass The travel class for the booking search.
 * @property checkInDate Hotel check-in date if hotel is added.
 * @property checkOutDate Hotel check-out date if hotel is added.
 */

@Serializable
data class  BookingSearchRequest(
    val userId: Int,
    val oneWay: Boolean,
    val nonStop: Boolean,
    val origin: Airport,
    val destination: Airport,
    val departureDate: LocalDate,
    val returnDate: LocalDate?,
    val adultsNumber: Int,
    val travelClass: String,
    val hotel: Boolean,
    val checkInDate: LocalDate? = null,
    val checkOutDate: LocalDate? = null
)
>>>>>>> development
