package com.travelsmartplus.services.apiResponses

import kotlinx.serialization.Serializable

/**
* API response models for Amadeus hotel offers. It is based on the model provided by the API and allows to serialise
* the response making it easier to handle it. - [API Reference](https://developers.amadeus.com/self-service/category/hotels/api-doc/hotel-search/api-reference)
*/

@Serializable
data class AmadeusHotelOffersResponse(
    val data: List<HotelOffer>,
    val self: String? = null
)

@Serializable
data class HotelOffer(
    val type: String,
    val hotel: Hotel,
    val available: Boolean,
    val offers: List<Offer>
)

@Serializable
data class Hotel(
    val type: String? = null,
    val hotelId: String,
    val chainCode: String? = null,
    val dupeId: String? = null,
    val name: String,
    val cityCode: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Serializable
data class Offer(
    val id: String,
    val checkInDate: String,
    val checkOutDate: String,
    val rateCode: String,
    val rateFamilyEstimated: RateFamilyEstimated? = null,
    val room: Room? = null,
    val guests: Guests? = null,
    val price: HotelPrice? = null,
    val policies: Policies? = null,
    val self: String? = null
)

@Serializable
data class RateFamilyEstimated(
    val code: String? = null,
    val type: String? = null
)

@Serializable
data class Room(
    val type: String? = null,
    val typeEstimated: TypeEstimated? = null,
    val description: Description? = null
)

@Serializable
data class TypeEstimated(
    val category: String? = null,
    val beds: Int? = null,
    val bedType: String? = null
)

@Serializable
data class Description(
    val text: String? = null,
    val lang: String? = null
)

@Serializable
data class Guests(
    val adults: Int? = null
)

@Serializable
data class HotelPrice(
    val currency: String? = null,
    val base: String? = null,
    val total: String? = null,
    val variations: Variations? = null
)

@Serializable
data class Variations(
    val average: Average? = null,
    val changes: List<Change>? = null
)

@Serializable
data class Average(
    val base: String? = null
)

@Serializable
data class Change(
    val startDate: String? = null,
    val endDate: String? = null,
    val total: String? = null
)

@Serializable
data class Policies(
    val paymentType: String? = null,
    val cancellation: Cancellation? = null
)

@Serializable
data class Cancellation(
    val description: Description? = null,
    val type: String? = null
)