package com.mobile.ziku.gpa.activities.map

import android.location.Location
import com.mobile.ziku.gpa.http.RetrofitService
import com.mobile.ziku.gpa.managers.DistanceComparator
import com.mobile.ziku.gpa.model.PlaceSearched
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class MapsDataManager @Inject constructor(
        val placesService: RetrofitService.PlacesService,
        @Named("API_PLACES_KEY") val API_KEY: String
) : MapsContractor.DataManager {

    companion object {
        const val MAX_RADIUS = 10 * 1000
        const val MAX_RESULT_ITEM = 3
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
                    emitter.onError(Exception("Empty result"))
                }
            } catch (exception: Exception) {
                emitter.onError(exception)
            } catch (exception: java.lang.Exception){
                emitter.onError(exception)
            }
        }
    }

    private fun getStringFromLocation(location: Location): String {
        return String.format(Locale.ENGLISH, "%f,%f", location.latitude, location.longitude)
    }

    private fun filterResult(results: List<PlaceSearched>, myLocation: Location): List<PlaceSearched> {
        if (results.size > 3) {
            return results.sortedWith(DistanceComparator(myLocation)).take(MAX_RESULT_ITEM)
        }
        return results
    }

}