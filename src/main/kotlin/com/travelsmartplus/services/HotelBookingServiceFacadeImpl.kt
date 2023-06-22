package com.travelsmartplus.services

import com.travelsmartplus.apis.AmadeusAuthenticationApi
import com.travelsmartplus.apis.HotelApi
import com.travelsmartplus.apis.apiResponses.AmadeusHotelListResponse
import com.travelsmartplus.apis.apiResponses.AmadeusHotelOffersResponse
import com.travelsmartplus.models.HotelBooking
import com.travelsmartplus.models.requests.BookingSearchRequest
import kotlinx.datetime.toLocalDate
import java.net.SocketTimeoutException

/**
 * Implementation of the [HotelBookingServiceFacade] interface. Handles data processing logic for API responses.
 * @author Gabriel Salas
 */

class HotelBookingServiceFacadeImpl : HotelBookingServiceFacade {

    private val hotelApi : HotelApi = HotelApi()

    override suspend fun getHotels(bookingSearchRequest: BookingSearchRequest): List<HotelBooking> {
        try {
            // Get a new authorisation token
            val token = AmadeusAuthenticationApi().call()

            // Get list with the hotel IDs in the city
            val hotelListResponse = hotelApi.getHotelList(token, bookingSearchRequest)
            val hotelIds = getHotelIds(hotelListResponse).shuffled().take(20) // Only 20 hotels for testing

            // Split hotel IDs into batches - This optimise calls and avoid timeout
            val batches = hotelIds.chunked(10)

            // List to store the results
            val hotelBookings = mutableListOf<HotelBooking>()

            // Process calls for each batch
            for (batch in batches) {
                val hotelOffersResponse = hotelApi.getHotelOffers(token, batch, bookingSearchRequest)
                val batchHotelBookings = createHotelBookingFromResponse(hotelOffersResponse)
                hotelBookings.addAll(batchHotelBookings)
            }

            return hotelBookings

        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception()
        }
    }

    override suspend fun getHotelIds(response: AmadeusHotelListResponse): List<String> {
        return response.data.mapNotNull { hotelResult ->
            hotelResult.hotelId
        }
    }

    private fun createHotelBookingFromResponse(
        response: AmadeusHotelOffersResponse
    ): List<HotelBooking> {
        return response.data.map { hotelOffer ->

            val nights = hotelOffer.offers[0].checkOutDate.toLocalDate().dayOfYear - hotelOffer.offers[0].checkInDate.toLocalDate().dayOfYear
            val totalPrice = hotelOffer.offers[0].price.total.toBigDecimal() * nights.toBigDecimal()

            // Create hotel booking
            HotelBooking(
                hotelName = hotelOffer.hotel.name,
                hotelChainCode = hotelOffer.hotel.chainCode,
                address = hotelOffer.hotel.latitude.toString(),
                checkInDate = hotelOffer.offers[0].checkInDate.toLocalDate(),
                checkOutDate = hotelOffer.offers[0].checkOutDate.toLocalDate(),
                roomType = hotelOffer.offers[0].room?.type,
                rate = hotelOffer.offers[0].price.total.toBigDecimal(),
                totalPrice = totalPrice,
                latitude = hotelOffer.hotel.latitude!!,
                longitude = hotelOffer.hotel.longitude!!
            )
        }
    }
}