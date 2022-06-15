package it.samuele794.scala.model.maps

expect fun getGeoHash(latLng: LatLng): String

/**
 * radius in meter
 */
expect fun getGeoHashQueryBounds(center: LatLng, radius: Double = 10.0): List<GeoQueryBounds>

expect fun getDistanceBetween(a: LatLng, b: LatLng): Double