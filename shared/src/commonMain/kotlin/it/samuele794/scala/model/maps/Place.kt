package it.samuele794.scala.model.maps

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize

@Parcelize
data class Place(
    val placeId: String,
    val placeName: String,
    val address: String,
    val latLng: LatLng
) : Parcelable

@Parcelize
data class LatLng(
    val lat: Double,
    val lng: Double
) : Parcelable