package com.mobile.ziku.gpa.model

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
        val location: Location? = null
)

data class Location(
        val lat: Double? = null,
        val lng: Double? = null
)
