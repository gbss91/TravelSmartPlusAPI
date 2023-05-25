package com.travelsmartplus.services.apiResponses

import kotlinx.serialization.Serializable

/**
 * API response models for Amadeus hotel list. It is based on the model provided by the API and allows to serialise
 * the response making it easier to handle it. - [API Reference](https://developers.amadeus.com/self-service/category/hotels/api-doc/hotel-list/api-reference)
 */

@Serializable
data class AmadeusHotelListResponse(
    val meta: MetaData,
    val data: List<HotelResult>
)

@Serializable
data class MetaData(
    val count: Int,
    val links: MetaLinks
)

@Serializable
data class MetaLinks(
    val self: String
)

@Serializable
data class HotelResult(
    val chainCode: String? = null,
    val iataCode: String? = null,
    val dupeId: Long? = null,
    val name: String? = null,
    val hotelId: String? = null,
    val geoCode: GeoCode? = null,
    val address: Address? = null,
    val distance: Distance? = null,
    val lastUpdate: kotlinx.datetime.LocalDateTime? = null
)

@Serializable
data class GeoCode(
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Serializable
data class Address(
    val countryCode: String? = null
)

@Serializable
data class Distance(
    val value: Double? = null,
    val unit: String? = null
)



