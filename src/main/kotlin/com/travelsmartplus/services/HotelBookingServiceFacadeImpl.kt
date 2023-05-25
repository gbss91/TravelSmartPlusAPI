package com.travelsmartplus.services

import com.travelsmartplus.models.HotelBooking
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.services.apiResponses.AmadeusHotelListResponse
import com.travelsmartplus.services.apiResponses.AmadeusHotelOffersResponse
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.json.Json

/**
 * Implementation of the [HotelBookingServiceFacade] interface. Handles data processing logic for API responses.
 * @author Gabriel Salas
 */

class HotelBookingServiceFacadeImpl : HotelBookingServiceFacade {

    private val hotelApi : HotelBookingApi = HotelBookingApi()

    override suspend fun getHotels(bookingSearchRequest: BookingSearchRequest): List<HotelBooking> {
        try {
            // Get a new authorisation token
            val token = AmadeusAuthentication().call()

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
                val batchHotelBookings = createHotelBookingFromResponse(hotelOffersResponse, bookingSearchRequest)
                hotelBookings.addAll(batchHotelBookings)
            }

            return hotelBookings

        } catch (e: IllegalStateException) {
            println(e.message)
            return emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    override suspend fun getHotelIds(response: AmadeusHotelListResponse): List<String> {
        return response.data.mapNotNull { hotelResult ->
            hotelResult.hotelId
        }
    }

    private fun createHotelBookingFromResponse(
        response: AmadeusHotelOffersResponse,
        bookingSearchRequest: BookingSearchRequest
    ): List<HotelBooking> {
        return response.data.map { hotelOffer ->

            // Create hotel booking
            HotelBooking(
                hotelName = hotelOffer.hotel.name,
                address = hotelOffer.hotel.latitude.toString(),
                checkInDate = hotelOffer.offers[0].checkInDate.toLocalDate(),
                checkOutDate = hotelOffer.offers[0].checkOutDate.toLocalDate(),
                rate = hotelOffer.offers[0].price!!.total!!.toBigDecimal(),
                totalPrice = hotelOffer.offers[0].price!!.total!!.toBigDecimal(),
                latitude = hotelOffer.hotel.latitude!!,
                longitude = hotelOffer.hotel.longitude!!
            )
        }
    }
}