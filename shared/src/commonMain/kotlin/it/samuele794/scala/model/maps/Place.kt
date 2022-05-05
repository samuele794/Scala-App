package it.samuele794.scala.model.maps

data class Place(
    val placeId: String,
    val placeName: String,
    val address: String,
    val latLng: LatLng
)

data class LatLng(
    val lat: Double,
    val lng: Double
)