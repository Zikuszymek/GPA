package com.mobile.ziku.gpa.managers

import android.location.Location
import com.mobile.ziku.gpa.model.PlaceSearched

class DistanceComparator(val myLocation: Location) : Comparator<PlaceSearched> {

    override fun compare(placeOne: PlaceSearched, placeTwo: PlaceSearched): Int {
        return compareDistance(placeOne, placeTwo, myLocation)
    }

    private fun compareDistance(placeOne: PlaceSearched, placeTwo: PlaceSearched, myLocation: Location): Int {
        val locationOne = placeOne.geometry?.location
        val locationTwo = placeTwo.geometry?.location
        val placeOneDistance = myLocation.distanceTo(locationOne)
        val placeTwoDistance = myLocation.distanceTo(locationTwo)
        return when {
            placeOneDistance > placeTwoDistance -> 1
            placeOneDistance == placeTwoDistance -> 0
            else -> -1
        }
    }

}