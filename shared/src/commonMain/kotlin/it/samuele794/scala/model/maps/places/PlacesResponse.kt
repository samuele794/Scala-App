package it.samuele794.scala.model.maps.places


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacesResponse(
    @SerialName("results")
    val placesResults: List<PlacesResult>,
    @SerialName("status")
    val status: String
)