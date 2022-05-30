package it.samuele794.scala.model.maps

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Place(
    val placeId: String,
    val placeName: String,
    val address: String,
    val latLng: LatLng,
    val geoHash: String
) : Parcelable

@Parcelize
@Serializable
data class LatLng(
    val lat: Double,
    val lng: Double
) : Parcelable