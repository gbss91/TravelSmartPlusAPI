package com.travelsmartplus.apis.apiResponses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API response models for Geocode Reverse Search. It is based on the model provided by the API and helps serialise
 * the response making it easier to handle it. - [API Reference](https://developers.google.com/maps/documentation/geocoding/requests-reverse-geocoding)
 */

@Serializable
data class FindAddressResponse(
    val results: List<AddressDetails>,
    val status: String
)

@Serializable
data class AddressDetails(
    @SerialName("address_components")
    val addressComponents: List<AddressComponent>,
    @SerialName("formatted_address")
    val formattedAddress: String,
    val geometry: AddressGeometry,
    @SerialName("place_id")
    val placeId: String,
    val types: List<String>
)

@Serializable
data class AddressComponent(
    @SerialName("long_name")
    val longName: String,
    @SerialName("short_name")
    val shortName: String,
    val types: List<String>
)

@Serializable
data class AddressGeometry(
    val location: AddressLocation,
    @SerialName("location_type")
    val locationType: String,
    val viewport: AddressViewport
)

@Serializable
data class AddressLocation(
    val lat: Double,
    val lng: Double
)

@Serializable
data class AddressViewport(
    val northeast: AddressLocation,
    val southwest: AddressLocation
)

