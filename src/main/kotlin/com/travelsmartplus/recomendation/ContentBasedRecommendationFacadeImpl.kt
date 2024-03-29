package com.travelsmartplus.recomendation

import com.travelsmartplus.dao.airline.AirlineDAOFacadeImpl
import com.travelsmartplus.dao.hotel.HotelDAOFacadeImpl
import com.travelsmartplus.models.Airline
import com.travelsmartplus.models.FlightBooking
import com.travelsmartplus.models.Hotel
import com.travelsmartplus.models.HotelBooking

/**
 * Implementation of [ContentBasedRecommendationFacade] interface.
 * Uses the [ContentBasedAlgorithm] recommend flights and hotels using user preferences. This can be used when there is not enough
 * history to train KNN algorithm
 *
 * @property flightContentBasedAlgorithm An instance of the [ContentBasedAlgorithm] for flight bookings.
 * @property hotelContentBasedAlgorithm An instance of the [ContentBasedAlgorithm] for hotel bookings.
 * @property airlineDao An instance of the [AirlineDAOFacadeImpl] for accessing airline data.
 * @property hotelDao An instance of the [HotelDAOFacadeImpl] for accessing hotel data.
 *
 * @author Gabriel Salas
 */

class ContentBasedRecommendationFacadeImpl: ContentBasedRecommendationFacade {

    private val flightContentBasedAlgorithm = ContentBasedAlgorithm<FlightBooking>()
    private val hotelContentBasedAlgorithm = ContentBasedAlgorithm<HotelBooking>()

    private val airlineDao = AirlineDAOFacadeImpl()
    private val hotelDao = HotelDAOFacadeImpl()
    private lateinit var allAirlines: List<Airline>
    private lateinit var allHotels: List<Hotel>

    override suspend fun recommendFlights(preferences: List<String>, flights: List<FlightBooking>): FlightBooking? {
        try {
            if (preferences.isEmpty() || flights.isEmpty()) {
                return null
            }

            // Initialise airlines
            allAirlines = airlineDao.getAllAirlines()

            // Create features matrix for user preferences
            val userPreferences = FeatureEncoder.encodeAirlinePreferences(allAirlines, preferences)

            // Create item features matrix for flights
            val itemFeatures = mutableListOf<DoubleArray>()
            for (flightBooking in flights) {
                val flightFeatures = getFlightFeatures(preferences, flightBooking)
                itemFeatures.add(flightFeatures)
            }

            // Return recommended flight booking
            return flightContentBasedAlgorithm.recommend(userPreferences, itemFeatures, flights)

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override suspend fun recommendHotels(preferences: List<String>, hotels: List<HotelBooking>): HotelBooking? {
        try {
            if (preferences.isEmpty() || hotels.isEmpty()) {
                return null
            }

            // Initialise hotels
            allHotels = hotelDao.getAllHotels()

            // Create features matrix for user preferences
            val userPreferences = FeatureEncoder.encodeHotelPreferences(allHotels, preferences)

            // Create item features matrix for flights
            val itemFeatures = mutableListOf<DoubleArray>()
            for (hotelBooking in hotels) {
                val hotelFeatures = getHotelFeatures(preferences, hotelBooking)
                itemFeatures.add(hotelFeatures)
            }

            // Return recommended hotel booking
            return hotelContentBasedAlgorithm.recommend(userPreferences, itemFeatures, hotels)

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }


    private fun getFlightFeatures(preferences: List<String>, flightBooking: FlightBooking): DoubleArray {
        val features = mutableListOf<Double>()

        // Encode hotels and add it to features - One-hot approach
        val flightsSegments = flightBooking.segments
        val airlineFeatures = FeatureEncoder.encodeAirlines(flightsSegments, allAirlines, preferences)
        features.addAll(airlineFeatures)

        return features.toDoubleArray()

    }

    private fun getHotelFeatures(preferences: List<String>, hotelBooking: HotelBooking): DoubleArray {
        val features = mutableListOf<Double>()

        // Encode hotels and add it to features - One-hot approach
        val hotelFeatures = FeatureEncoder.encodeHotels(hotelBooking, allHotels, preferences)
        features.addAll(hotelFeatures)

        return features.toDoubleArray()
    }


}