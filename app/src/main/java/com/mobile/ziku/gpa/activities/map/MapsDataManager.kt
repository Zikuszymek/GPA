package com.mobile.ziku.gpa.activities.map

import android.location.Location
import com.mobile.ziku.gpa.http.RetrofitService
import com.mobile.ziku.gpa.model.PlaceSearched
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import kotlin.Comparator

class MapsDataManager @Inject constructor(
        val placesService: RetrofitService.PlacesService
) : MapsContractor.DataManager {

    companion object {
        const val MAX_RADIUS = 10 * 1000
        const val MAX_RESULT_ITEM = 3
        const val API_KEY = "AIzaSyCLkt-UURtUeSbwLb-8u1dvtD5m6fm8pgg"

    }

    override fun getDataForPlace(place: String, location: Location): Single<List<PlaceSearched>> {
        return Single.create<List<PlaceSearched>> { emitter ->
            try {
                val locationString = getStringFromLocation(location)
                val result = placesService.searchPlacesByName(locationString, MAX_RADIUS, place, API_KEY).blockingGet()
                if (result.results != null) {
                    val filteredResult = filterResult(result.results, location)
                    emitter.onSuccess(filteredResult)
                } else {
                    emitter.onError(Exception("exception"))
                }
            } catch (exception: Exception) {
                emitter.onError(exception)
            }
        }
    }

    private fun getStringFromLocation(location: Location): String {
        return String.format(Locale.ENGLISH, "%f,%f", location.latitude, location.longitude)
    }

    private fun filterResult(results: List<PlaceSearched>, myLocation: Location): List<PlaceSearched> {
        if (results.size > 3) {
            return results.sortedWith(Comparator { placeOne, placeTwo ->
                compareDistance(placeOne, placeTwo, myLocation)
            }).take(MAX_RESULT_ITEM)
        }
        return results
    }

    private fun compareDistance(placeOne: PlaceSearched, placeTwo: PlaceSearched, myLocation: Location): Int {
        val locationOne = getLocationFromPlace(placeOne, "One")
        val locationTwo = getLocationFromPlace(placeTwo, "Two")
        val placeOneDistance = myLocation.distanceTo(locationOne)
        val placeTwoDistance = myLocation.distanceTo(locationTwo)
        return when {
            placeOneDistance > placeTwoDistance -> 1
            placeOneDistance == placeTwoDistance -> 0
            else -> -1
        }
    }

    private fun getLocationFromPlace(placeSearched: PlaceSearched, locationName: String): Location {
        val location = Location(locationName)
        location.apply {
            latitude = placeSearched.geometry?.location?.lat ?: 0.0
            longitude = placeSearched.geometry?.location?.lng ?: 0.0
        }
        return location
    }

}