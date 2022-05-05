package it.samuele794.scala.api.maps

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface GoogleMapsApi {
    @GET("maps/api/place/textsearch/json")
    suspend fun getPlaces(
        @Query("key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String
    ): String
}