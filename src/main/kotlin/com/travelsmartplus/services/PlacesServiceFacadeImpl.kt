package com.travelsmartplus.services

import com.travelsmartplus.apis.PlacesApi
import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.requests.BookingSearchRequest

/**
 * Implementation of the [PlacesServiceFacade] interface. Process the image URL for bookings.
 * @author Gabriel Salas
 */

class PlacesServiceFacadeImpl : PlacesServiceFacade {

    private val placesApi: PlacesApi = PlacesApi()

    // Add the image URL to each booking result. Bookings will be sent over HTTPS
    override suspend fun processBookingsImage(allBookings: List<Booking>): List<Booking> {

        val updatedBookings = mutableListOf<Booking>()

        try {
            for (booking in allBookings) {
                // Get the image for destination city
                val imageUrl = placesApi.getImageUrl(booking.destination.city)

                // Add the image URL to the booking object and add it to updated bookings
                booking.imageUrl = imageUrl
                updatedBookings.add(booking)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return updatedBookings
    }
}