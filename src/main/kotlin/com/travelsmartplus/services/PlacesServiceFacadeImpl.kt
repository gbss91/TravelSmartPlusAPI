package com.travelsmartplus.services

import com.travelsmartplus.apis.PlacesApi
import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.HotelBooking

/**
 * Implementation of the [PlacesServiceFacade] interface. Process the image URL and address for bookings.
 * @author Gabriel Salas
 */

class PlacesServiceFacadeImpl : PlacesServiceFacade {

    private val placesApi: PlacesApi = PlacesApi()

    // Add the image URL to each booking result. Bookings will be sent over HTTPS
    override suspend fun processBookingsImage(allBookings: List<Booking>): List<Booking> {

        val updatedBookings = mutableListOf<Booking>()

        for (booking in allBookings) {
            try {
                // Get the image for the destination city
                val imageUrl = placesApi.getImageUrl(booking.destination.city)
                booking.imageUrl = imageUrl

            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Add the booking to updated bookings
            updatedBookings.add(booking)
        }

        return updatedBookings
    }

    override suspend fun getAddress(hotelBookings: List<HotelBooking>): List<HotelBooking> {

        val updatedHotelBookings = mutableListOf<HotelBooking>()

        for (hotelBooking in hotelBookings) {

            try {
                // Get address and add it to booking
                val formattedAddress = placesApi.getAddress(hotelBooking.latitude, hotelBooking.longitude) ?: ""
                hotelBooking.address = formattedAddress

            } catch (e: Exception) {
                e.printStackTrace()
            }

            updatedHotelBookings.add(hotelBooking)
        }

        return updatedHotelBookings
    }
}