package com.travelsmartplus.apis.apiResponses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API response models for Google Places API.
 */

@Serializable
data class FindPlaceResponse(
    val candidates: List<PlaceCandidate>,
    val status: String
)

@Serializable
data class PlaceCandidate(
    val photos: List<Photo>
)

@Serializable
data class Photo(
    val height: Int,
    @SerialName("html_attributions")
    val htmlAttributions: List<String>,
    @SerialName("photo_reference")
    val photoReference: String,
    val width: Int
)
