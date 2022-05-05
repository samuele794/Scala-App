package it.samuele794.scala.utils

import it.samuele794.scala.model.maps.LatLng
import com.google.android.gms.maps.model.LatLng as MapLatLng

fun LatLng.toLatLng(): MapLatLng = MapLatLng(lat, lng)