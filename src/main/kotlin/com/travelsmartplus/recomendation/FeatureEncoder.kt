package com.travelsmartplus.recomendation

import com.travelsmartplus.models.Airline
import com.travelsmartplus.models.FlightSegment
import com.travelsmartplus.models.Hotel
import com.travelsmartplus.models.HotelBooking

/**
 * Encodes features as feature vector. This is uses to make recommendations.
 *
 * @author Gabriel Salas
 */

object FeatureEncoder {

    // Encode Airline as feature vector using One-hot approach
    fun encodeAirlines(segments: List<FlightSegment>, allAirlines: List<Airline>, preferredAirlines: List<String>): List<Double> {
        val encodedFeatures = mutableListOf<Double>()

        for (airline in allAirlines) {

            // Increase weight for preferred airlines, with the first one having the highest weight
            val weight = if (preferredAirlines.contains(airline.airlineName)) {
                preferredAirlines.size - preferredAirlines.indexOf(airline.airlineName).toDouble()
            } else 1.0

            val encodedFeature = if (segments.any { it.flights.any { flight ->
                    flight.carrierIataCode == airline.iataCode
                } }) weight else 0.0

            encodedFeatures.add(encodedFeature)
        }
        return encodedFeatures
    }

    // Encode Hotels as feature vector using One-hot approach
    fun encodeHotels(hotelBooking: HotelBooking, allHotels: List<Hotel>, preferredHotels: List<String>): List<Double> {
        val encodedFeatures = mutableListOf<Double>()

        for (hotel in allHotels) {

            // Increase weight for preferred hotels, with the first one having the highest weight
            val weight = if (preferredHotels.contains(hotel.hotelChain)) {
                preferredHotels.size - preferredHotels.indexOf(hotel.hotelChain).toDouble() //
            } else 1.0

            val encodedFeature = if (hotelBooking.hotelChainCode == hotel.code) weight else 0.0
            encodedFeatures.add(encodedFeature)
        }
        return encodedFeatures
    }

    // Encode airline preferences
    fun encodeAirlinePreferences(allAirlines: List<Airline>, preferredAirlines: List<String>): DoubleArray {
        val userPreferences = DoubleArray(allAirlines.size)

        for (i in allAirlines.indices) {
            val airline = allAirlines[i]

            // Increase weight for preferred airlines, with the first one having the highest weight
            val weight = if (preferredAirlines.contains(airline.airlineName)) {
                preferredAirlines.size - preferredAirlines.indexOf(airline.airlineName) + 1.0
            } else {
                0.0
            }

            userPreferences[i] = weight
        }

        return userPreferences
    }
    // Encode hotel preferences
    fun encodeHotelPreferences(allHotels: List<Hotel>, preferredHotels: List<String>): DoubleArray {
        val userPreferences = DoubleArray(allHotels.size)

        for (i in allHotels.indices) {
            val hotel = allHotels[i]

            // Increase weight for preferred hotels, with the first one having the highest weight
            val weight = if (preferredHotels.contains(hotel.hotelChain)) {
                preferredHotels.size - preferredHotels.indexOf(hotel.hotelChain) + 1.0
            } else {
                0.0
            }

            userPreferences[i] = weight
        }

        return userPreferences
    }

    // Encode Travel Class
    fun encodeTravelClass(travelClass: String): Double {
        return when (travelClass) {
            "ECONOMY" -> 1.0
            "PREMIUM_ECONOMY" -> 2.0
            "BUSINESS" -> 3.0
            "FIRST" -> 4.0
            else -> 0.0
        }
    }

}