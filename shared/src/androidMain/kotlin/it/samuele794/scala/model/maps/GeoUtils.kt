package it.samuele794.scala.model.maps

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation

actual fun getGeoHash(latLng: LatLng): String {
    return GeoFireUtils.getGeoHashForLocation(GeoLocation(latLng.lat, latLng.lng))
}

actual fun getGeoHashQueryBounds(center: LatLng, radius: Double): List<GeoQueryBounds> {
    return GeoFireUtils.getGeoHashQueryBounds(GeoLocation(center.lat, center.lng), radius).map {
        GeoQueryBounds(it.startHash, it.endHash)
    }
}