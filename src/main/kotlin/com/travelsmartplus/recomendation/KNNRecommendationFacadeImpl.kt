package com.travelsmartplus.recomendation

import com.travelsmartplus.dao.airline.AirlineDAOFacadeImpl
import com.travelsmartplus.models.*

/**
 * Implementation of [KNNRecommendationFacade] interface.
 * Uses the [KNNAlgorithm] to train algorithm and make flight and hotel predictions.
 * @author Gabriel Salas
 */

class KNNRecommendationFacadeImpl : KNNRecommendationFacade {
    private val flightKnnAlgorithm = KNNAlgorithm<FlightBooking>()
    private val hotelKnnAlgorithm = KNNAlgorithm<HotelBooking>()
    private val airlineDao = AirlineDAOFacadeImpl()
    private lateinit var allAirlines: List<Airline>

    // Train algorithm using previous bookings - There is a separate algorithm for flights and hotels.
    override suspend fun trainModel(previousBookings: List<Booking>, k: Int) {
        try {
            allAirlines = airlineDao.getAllAirlines() // All possible airlines in database

            val flightLabeledInstances = previousBookings.map { booking ->
                LabeledInstance(
                    features = getFlightFeatures(booking.flightBooking, allAirlines),
                    label = booking.flightBooking
                )
            }

            flightKnnAlgorithm.train(flightLabeledInstances, k)

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

                hotelKnnAlgorithm.train(hotelLabeledInstances, k)
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
                features = getFlightFeatures(flight, allAirlines),
                label = flight
            )
        }

        // Predict flight with the highest predicted label
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

        // Predict hotel with the highest predicted label
        val predictedHotel = hotelKnnAlgorithm.predict(instances)
        return predictedHotel?.label
    }


    // Extract relevant features for flights
    private fun getFlightFeatures(flightBooking: FlightBooking, allAirlines: List<Airline>): List<Double> {
        val features = mutableListOf<Double>()

        // Encode oneWay to numerical value (0 or 1) and add it to features
        val oneWayFeature = if (flightBooking.oneWay) 1.0 else 0.0
        features.add(oneWayFeature)

        // Price
        features.add(flightBooking.totalPrice.toDouble())

        // Encode airlines as numerical value and add it to features - One-hot approach
        val flightsSegments = flightBooking.segments
        val airlineFeatures = encodeAirlines(flightsSegments, allAirlines)
        features.addAll(airlineFeatures)

        // Encode travel class as a numerical value and add it to features
        val travelClassFeature = encodeTravelClass(flightBooking.travelClass)
        features.add(travelClassFeature)

        return features
    }

    private fun getHotelFeatures(hotelBooking: HotelBooking): List<Double> {
        val features = mutableListOf<Double>()

        // Rate and price
        features.add(hotelBooking.rate.toDouble())
        features.add(hotelBooking.totalPrice.toDouble())

        return features
    }

    private fun encodeAirlines(segments: List<FlightSegment>, allAirlines: List<Airline>): List<Double> {
        val encodedFeatures = mutableListOf<Double>()

        for (airline in allAirlines) {
            val encodedFeature = if (segments.any { it.flights.any { flight ->
                flight.carrierIataCode == airline.iataCode
            } }) 1.0 else 0.0
            encodedFeatures.add(encodedFeature)
        }
        return encodedFeatures
    }


    private fun encodeTravelClass(travelClass: String): Double {
        return when (travelClass) {
            "ECONOMY" -> 0.0
            "PREMIUM_ECONOMY" -> 1.0
            "BUSINESS" -> 2.0
            "FIRST" -> 3.0
            else -> 4.0
        }
    }
}