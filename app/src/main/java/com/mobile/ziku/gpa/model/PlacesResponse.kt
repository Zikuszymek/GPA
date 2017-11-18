package com.mobile.ziku.gpa.model

import android.location.Location
import com.mobile.ziku.gpa.moshi.InToLocation

data class PlacesResponse(
        val results: List<PlaceSearched>? = null
)

data class PlaceSearched(
        val geometry: Geometry? = null,
        val icon: String? = null,
        val id: String? = null,
        val name: String? = null,
        val vicinity: String? = null
)

data class Geometry(
        @InToLocation val location: Location? = null
)
