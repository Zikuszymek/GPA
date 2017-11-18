package com.mobile.ziku.gpa.activities.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mobile.ziku.gpa.activities.BasePresenter
import com.mobile.ziku.gpa.activities.BaseView
import com.mobile.ziku.gpa.enums.MessageType
import com.mobile.ziku.gpa.model.PlaceSearched
import io.reactivex.Single

interface MapsContractor {

    interface View : BaseView<Presenter>{
        fun displayMessage(messageType: MessageType)
        fun handleSearchResult(placeResponse: List<PlaceSearched>)
        fun addMarkerToMap(markerOptions: MarkerOptions) : Marker?
        fun moveMapCameraToLocation(latLng: LatLng, zoomLevel: Float)
        fun moveMapCameraToLocation(bounds : LatLngBounds)
    }

    interface Presenter : BasePresenter<View>{
        fun searchForPlace(place : String)
        fun updateCurrentLocalization()
    }

    interface DataManager{
        fun getDataForPlace(place: String, location: Location) : Single<List<PlaceSearched>>
    }
}