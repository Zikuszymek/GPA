package com.mobile.ziku.gpa.activities.map

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mobile.ziku.gpa.enums.MessageType
import com.mobile.ziku.gpa.model.PlaceSearched
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class MapsPresenter @Inject constructor(
        val compositeDisposable: CompositeDisposable,
        val dataManager: MapsDataManager,
        val fusedLocationProviderClient: FusedLocationProviderClient,
        @Named("MyLocation") val myLocalization : String
) : MapsContractor.Presenter {

    var view: MapsContractor.View? = null

    private var myMarker: Marker? = null
    private var locationMarkers = mutableListOf<Marker>()

    override fun attachView(view: MapsContractor.View) {
        this.view = view
    }

    override fun removeView() {
        view = null
    }

    @SuppressWarnings("MissingPermission")
    override fun updateCurrentLocalization() {
        fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { updateMyCurrentLocationAnMoveCamerra(it) }
                .addOnFailureListener { view?.displayMessage(MessageType.LOCALIZATION_ERROR) }
    }

    @SuppressWarnings("MissingPermission")
    override fun searchForPlace(place: String) {
        fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { searchForQuery(place, it) }
                .addOnFailureListener { view?.displayMessage(MessageType.LOCALIZATION_ERROR) }
    }

    private fun searchForQuery(place: String, location: Location) {
        compositeDisposable.add(
                dataManager.getDataForPlace(place, location)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.isNotEmpty()) {
                                handleMarkersUpdate(it)
                                view?.handleSearchResult(it)
                            } else {
                                view?.displayMessage(MessageType.NO_RESULT)
                            }
                        }, {
                            view?.displayMessage(MessageType.SEARCH_PROBLEM)
                        })
        )
    }

    fun updateMyCurrentLocationAnMoveCamerra(location: Location?) {
        if (location != null) {
            val myLocation = LatLng(location.latitude, location.longitude)
            if (myMarker != null) {
                myMarker?.position = myLocation
            } else {
                val markerOptions = MarkerOptions().position(myLocation).title(myLocalization)
                myMarker = view?.addMarker(markerOptions)
            }
            view?.moveCameraToLocation(myLocation, MapsActivity.CITY_ZOOM)
        } else {
            myMarker?.remove()
        }
    }

    private fun handleMarkersUpdate(placeResponse: List<PlaceSearched>) {
        locationMarkers.forEach { it.remove() }
        locationMarkers.clear()
        placeResponse.forEach {
            val location = it.geometry?.location
            if (location?.lat != null && location.lng != null) {
                val marker = MarkerOptions().position(LatLng(location.lat, location.lng))
                        .title(it.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                val markerOnMap = view?.addMarker(marker)
                markerOnMap?.let { locationMarkers.add(markerOnMap) }
            }
        }
    }
}