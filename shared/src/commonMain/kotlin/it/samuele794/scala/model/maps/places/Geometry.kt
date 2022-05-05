package it.samuele794.scala.model.maps.places


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    @SerialName("location")
    val location: Location,
    @SerialName("viewport")
    val viewport: Viewport
)