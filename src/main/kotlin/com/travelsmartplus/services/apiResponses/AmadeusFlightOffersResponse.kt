package com.travelsmartplus.services.apiResponses

import kotlinx.serialization.Serializable

@Serializable
data class AmadeusFlightOffersResponse(
    val data: List<FlightOffer>,
    val dictionaries: Dictionaries
)

@Serializable
data class FlightOffer(
    val type: String,
    val id: String,
    val source: String,
    val instantTicketingRequired: Boolean,
    val nonHomogeneous: Boolean,
    val oneWay: Boolean,
    val lastTicketingDate: String,
    val numberOfBookableSeats: Int,
    val itineraries: List<Itinerary>,
    val price: Price,
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
    val aircraft: Aircraft,
    val operating: OperatingCarrier,
    val duration: String,
    val id: String,
    val numberOfStops: Int,
    val blacklistedInEU: Boolean
)

@Serializable
data class FlightEndpoint(
    val iataCode: String,
    val terminal: String,
    val at: String
)

@Serializable
data class Aircraft(
    val code: String
)

@Serializable
data class OperatingCarrier(
    val carrierCode: String
)

@Serializable
data class Price(
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
    val includedCheckedBagsOnly: Boolean
)

@Serializable
data class Dictionaries(
    val locations: Map<String, Location>,
    val aircraft: Map<String, AircraftDictionaryEntry>,
    val currencies: Map<String, Currency>,
    val carriers: Map<String, Carrier>,
    val serviceClasses: Map<String, ServiceClass>,
    val trafficTypes: Map<String, TrafficType>
)

@Serializable
data class Location(
    val cityCode: String,
    val countryCode: String
)

@Serializable
data class AircraftDictionaryEntry(
    val iataCode: String,
    val icaoCode: String? = null
)

@Serializable
data class Currency(
    val code: String,
    val name: String
)

@Serializable
data class Carrier(
    val code: String,
    val businessName: String,
    val commonName: String? = null
)

@Serializable
data class ServiceClass(
    val code: String,
    val description: String
)

@Serializable
data class TrafficType(
    val code: String,
    val description: String
)
