package it.samuele794.scala.repository

import it.samuele794.scala.api.maps.GoogleMapsApi
import it.samuele794.scala.model.maps.LatLng
import it.samuele794.scala.model.maps.Place
import it.samuele794.scala.model.maps.places.PlacesResponse
import kotlinx.serialization.json.Json

class GoogleMapsRepository(
    private val googleMapsApi: GoogleMapsApi,
    private val serializer: Json,
    private val googleMapsApiKey: String,
    private val appLanguage: String = "it"
) {

    suspend fun getPlaces(query: String): List<Place> {
        //Mitigation for ByteBuffer serializer
        val placesResponse = serializer.decodeFromString(
            PlacesResponse.serializer(),
            googleMapsApi.getPlaces(googleMapsApiKey, query, appLanguage)
        )

        val places = placesResponse.placesResults.map {
            Place(
                placeId = it.placeId,
                placeName = it.name,
                address = it.formattedAddress,
                latLng = LatLng(it.geometry.location.lat, it.geometry.location.lng)
            )
        }

        return places
    }
}