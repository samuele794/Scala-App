package it.samuele794.scala.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.*
import it.samuele794.scala.model.maps.LatLng
import it.samuele794.scala.model.maps.Place
import it.samuele794.scala.model.maps.getGeoHashQueryBounds
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


interface PlaceRepository {

    suspend fun getPlace(documentReference: String): Place

    suspend fun getPlacesByBounds(center: LatLng, range: Double = 10.0, limit: Int = 10): List<QuerySnapshot>

    suspend fun addPlace(place: Place): DocumentReference
}

class PlaceRepositoryImpl : PlaceRepository {

    override suspend fun getPlace(documentReference: String): Place {
        return Firebase.firestore.document(documentReference)
            .get()
            .data(Place.serializer())
    }

    override suspend fun addPlace(place: Place): DocumentReference {
        val doc = Firebase.firestore.collection(PLACE_COLLECTION)
            .document(place.geoHash)
        doc.set(Place.serializer(), place)
        return doc
    }

    override suspend fun getPlacesByBounds(center: LatLng, range: Double, limit: Int): List<QuerySnapshot> =
        coroutineScope {
            val bounds = getGeoHashQueryBounds(center, range)

            val query = mutableListOf<Deferred<QuerySnapshot>>()

            bounds.forEach {
                query.add(
                    async {
                        Firebase.firestore.collection(PLACE_COLLECTION)
                            .orderBy(Place::geoHash.name)
                            .startAt(it.startHash)
                            .endAt(it.endHash)
                            .get()
                    }
                )
            }

            return@coroutineScope query.awaitAll()

        }

    companion object {
        const val PLACE_COLLECTION = "places"
    }
}