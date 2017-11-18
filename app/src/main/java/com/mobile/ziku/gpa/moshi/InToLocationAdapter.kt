package com.mobile.ziku.gpa.moshi

import android.location.Location
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class InToLocationAdapter {

    @ToJson
    fun toJson(@InToLocation location: Location): LocationMoshi {
        return LocationMoshi(location.latitude, location.longitude)
    }

    @FromJson
    @InToLocation
    fun fromJson(locationMoshi: LocationMoshi): Location {
        return Location("").apply {
            longitude = locationMoshi.lng ?: 0.0
            latitude = locationMoshi.lat ?: 0.0
        }
    }

    data class LocationMoshi(
            val lat: Double? = null,
            val lng: Double? = null
    )
}