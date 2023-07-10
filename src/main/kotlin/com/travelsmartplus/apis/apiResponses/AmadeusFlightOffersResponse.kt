package com.travelsmartplus.apis.apiResponses

import kotlinx.serialization.Serializable

/**
 * API response models for Amadeus flight offers. It is based on the model provided by the API and allows to serialise
 * the response making it easier to handle it. - [API Reference](https://developers.amadeus.com/self-service/category/flights/api-doc/flight-offers-search/api-reference)
 */

@Serializable
data class AmadeusFlightOffersResponse(
    val data: List<FlightOffer>,
    val dictionaries: Dictionaries? = null
)

@Serializable
data class FlightOffer(
    val type: String,
    val id: String,
    val source: String? = null,
    val instantTicketingRequired: Boolean? = null,
    val nonHomogeneous: Boolean? = null,
    val oneWay: Boolean,
    val lastTicketingDate: String? = null,
    val numberOfBookableSeats: Int? = null,
    val itineraries: List<Itinerary>,
    val price: FlightPrice,
    val pricingOptions: PricingOptions,
    val validatingAirlineCodes: List<String>? = null
)

@Serializable
data class Itinerary(
    val duration: String,
    val segments: List<Segment>
)

@Serializable
data class Segment(
    val departure: FlightEndpoint,
    val arrival: FlightEndpoint,
    val carrierCode: String,
    val number: String,
    val aircraft: Aircraft? = null,
    val operating: OperatingCarrier? = null,
    val duration: String,
    val id: String,
    val numberOfStops: Int? = null,
    val blacklistedInEU: Boolean? = null
)

@Serializable
data class FlightEndpoint(
    val iataCode: String,
    val terminal: String? = null,
    val at: String
)

@Serializable
data class Aircraft(
    val code: String? = null
)

@Serializable
data class OperatingCarrier(
    val carrierCode: String? = null
)

@Serializable
data class FlightPrice(
    val currency: String,
    val total: String,
    val base: String,
    val fees: List<Fee>,
    val grandTotal: String
)

@Serializable
data class Fee(
    val amount: String,
    val type: String
)

@Serializable
data class PricingOptions(
    val fareType: List<String>,
    val includedCheckedBagsOnly: Boolean? = null
)

@Serializable
data class Dictionaries(
    val locations: Map<String, Location>? = null,
    val aircraft: Map<String, String>? = null,
    val currencies: Map<String, String>? = null,
    val carriers: Map<String, String>? = null
)

@Serializable
data class Location(
    val cityCode: String,
    val countryCode: String
)
