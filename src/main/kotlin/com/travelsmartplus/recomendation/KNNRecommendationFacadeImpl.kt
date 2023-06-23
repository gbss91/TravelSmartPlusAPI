package com.travelsmartplus.recomendation

import com.travelsmartplus.dao.airline.AirlineDAOFacadeImpl
import com.travelsmartplus.dao.hotel.HotelDAOFacadeImpl
import com.travelsmartplus.models.*

/**
 * Implementation of [KNNRecommendationFacade] interface.
 * Uses the [KNNAlgorithm] to train algorithm and make flight and hotel predictions based on previous bookings and
 * preferred hotels and airlines.
 *
 * @property flightKnnAlgorithm An instance of the [KNNAlgorithm] for flight bookings.
 * @property hotelKnnAlgorithm An instance of the [KNNAlgorithm] for hotel bookings.
 * @property airlineDao An instance of the [AirlineDAOFacadeImpl] for accessing airline data.
 * @property hotelDao An instance of the [HotelDAOFacadeImpl] for accessing hotel data.
 *
 * @author Gabriel Salas
 */

class KNNRecommendationFacadeImpl : KNNRecommendationFacade {

    private val flightKnnAlgorithm = KNNAlgorithm<FlightBooking>()
    private val hotelKnnAlgorithm = KNNAlgorithm<HotelBooking>()

    private val airlineDao = AirlineDAOFacadeImpl()
    private val hotelDao = HotelDAOFacadeImpl()
    private lateinit var allAirlines: List<Airline>
    private lateinit var allHotels: List<Hotel>
    private lateinit var preferredAirlines: Set<String>
    private lateinit var preferredHotels: Set<String>

    // Train algorithm using previous bookings
    override suspend fun trainModel(
        previousBookings: List<Booking>,
        preferredAirlines: Set<String>,
        preferredHotels: Set<String>
    ) {
        try {
            // Initialise airlines and preferences
            allAirlines = airlineDao.getAllAirlines()
            allHotels = hotelDao.getAllHotels()
            this.preferredAirlines = preferredAirlines
            this.preferredHotels = preferredHotels

            val flightLabeledInstances = previousBookings.map { booking ->
                LabeledInstance(
                    features = getFlightFeatures(booking.flightBooking),
                    label = booking.flightBooking
                )
            }
            flightKnnAlgorithm.train(flightLabeledInstances)

            // Only train hotels if there are hotels in bookings
            if (previousBookings.any { it.hotelBooking != null }) {
                val hotelLabeledInstances = previousBookings.mapNotNull { booking ->
                    booking.hotelBooking?.let { hotelBooking ->
                        LabeledInstance(
                            features = getHotelFeatures(hotelBooking),
                            label = hotelBooking
                        )
                    }
                }
                hotelKnnAlgorithm.train(hotelLabeledInstances)
            }
        } catch (e:Exception) {
            e.printStackTrace()
            throw IllegalStateException("Unable to train algorithm")
        }
    }

    // Choose flight based on previous bookings
    override fun predict(flights: List<FlightBooking>): FlightBooking? {
        // Iterate over the flights to get features
        val instances = flights.map { flight ->
            LabeledInstance(
                features = getFlightFeatures(flight),
                label = flight
            )
        }

        // Predict flight with the closest neighbour
        val predictedFlight = flightKnnAlgorithm.predict(instances)
        return predictedFlight?.label
    }

    // Choose hotel based on previous bookings
    override fun predict(hotels: List<HotelBooking>): HotelBooking? {
        // Iterate over hotels to get features
        val instances = hotels.map { hotel ->
            LabeledInstance(
                features = getHotelFeatures(hotel),
                label = hotel
            )
        }

        // Predict flight with the closest neighbour
        val predictedHotel = hotelKnnAlgorithm.predict(instances)
        return predictedHotel?.label
    }


    // Extract and normalise relevant features for flights
    private fun getFlightFeatures(flightBooking: FlightBooking): List<Double> {
        val features = mutableListOf<Double>()

        // Encode oneWay to numerical value (0 or 1) and add it to features
        val oneWayFeature = if (flightBooking.oneWay) 1.0 else 0.0
        features.add(oneWayFeature)

        // Add Price feature
        features.add(flightBooking.totalPrice.toDouble())

        // Encode airlines as numerical value and add it to features - One-hot approach
        val flightsSegments = flightBooking.segments
        val airlineFeatures = encodeAirlines(flightsSegments)
        features.addAll(airlineFeatures)

        // Encode travel class as a numerical value and add it to features
        val travelClassFeature = encodeTravelClass(flightBooking.travelClass)
        features.add(travelClassFeature)

        // Return normalised features
        return FeatureScaling.minMaxNormalization(features)
    }

    // Extract and normalise relevant features for hotels
    private fun getHotelFeatures(hotelBooking: HotelBooking): List<Double> {
        val features = mutableListOf<Double>()

        // Rate and price
        features.add(hotelBooking.rate.toDouble())
        features.add(hotelBooking.totalPrice.toDouble())

        // Encode hotels and add it to features - One-hot approach
        val hotelFeatures = encodeHotels(hotelBooking)
        features.addAll(hotelFeatures)

        // Return normalised features
        return FeatureScaling.minMaxNormalization(features)
    }

    private fun encodeAirlines(segments: List<FlightSegment>): List<Double> {
        val encodedFeatures = mutableListOf<Double>()

        for (airline in allAirlines) {

            // Increase weight for preferred airlines
            val weight = if (preferredAirlines.contains(airline.iataCode)) 2.0 else 1.0

            val encodedFeature = if (segments.any { it.flights.any { flight ->
                flight.carrierIataCode == airline.iataCode
            } }) weight else 0.0

            encodedFeatures.add(encodedFeature)
        }
        return encodedFeatures
    }

    private fun encodeHotels(hotelBooking: HotelBooking): List<Double> {
        val encodedFeatures = mutableListOf<Double>()

        for (hotel in allHotels) {

            // Increase weight for preferred hotels
            val weight = if (preferredHotels.contains(hotelBooking.hotelChainCode)) 2.0 else 1.0
            val encodedFeature = if (hotelBooking.hotelChainCode == hotel.hotelChain) weight else 0.0
            encodedFeatures.add(encodedFeature)
        }
        return encodedFeatures
    }


    private fun encodeTravelClass(travelClass: String): Double {
        return when (travelClass) {
            "ECONOMY" -> 1.0
            "PREMIUM_ECONOMY" -> 2.0
            "BUSINESS" -> 3.0
            "FIRST" -> 4.0
            else -> 0.0
        }
    }
}