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
import com.google.android.gms.maps.model.LatLngBounds
import com.mobile.ziku.gpa.di.modules.AppModule.Companion.MY_LOCATION

class MapsPresenter @Inject constructor(
        val compositeDisposable: CompositeDisposable,
        val dataManager: MapsDataManager,
        val fusedLocationProviderClient: FusedLocationProviderClient,
        @Named(MY_LOCATION) val myLocalization: String
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
                .addOnSuccessListener { updateMyCurrentLocationAnMoveCamera(it) }
                .addOnFailureListener { view?.displayMessage(MessageType.LOCALIZATION_ERROR) }
    }

    @SuppressWarnings("MissingPermission")
    override fun searchForPlace(place: String) {
        fusedLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    searchForPlace(place, it)
                    updateMyCurrentLocation(it)
                }
                .addOnFailureListener { view?.displayMessage(MessageType.LOCALIZATION_ERROR) }
    }

    private fun searchForPlace(place: String, location: Location) {
        compositeDisposable.add(
                dataManager.getDataForPlace(place, location)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it.isNotEmpty()) {
                                handleMarkersUpdate(it)
                                zoomMapAccordingToMarkers()
                                view?.handleSearchResult(it)
                            } else {
                                deleteAllMarkers()
                                view?.displayMessage(MessageType.NO_RESULT)
                            }
                        }, {
                            deleteAllMarkers()
                            view?.displayMessage(MessageType.SEARCH_PROBLEM)
                        })
        )
    }

    private fun updateMyCurrentLocationAnMoveCamera(location: Location?) {
        val myLocation = updateMyCurrentLocation(location)
        if (myLocation != null) {
            view?.moveMapCameraToLocation(myLocation, MapsActivity.CITY_ZOOM)
        } else {
            view?.displayMessage(MessageType.LOCALIZATION_ERROR)
        }
    }

    private fun updateMyCurrentLocation(location: Location?): LatLng? {
        if (location != null) {
            val myLocation = LatLng(location.latitude, location.longitude)
            if (myMarker != null) {
                myMarker?.position = myLocation
            } else {
                val markerOptions = MarkerOptions().position(myLocation).title(myLocalization)
                myMarker = view?.addMarkerToMap(markerOptions)
            }
            return myLocation
        } else {
            myMarker?.remove()
            return null
        }
    }

    private fun handleMarkersUpdate(placeResponse: List<PlaceSearched>) {
        deleteAllMarkers()
        placeResponse.forEach {
            val location = it.geometry?.location
            if (location?.latitude != null) {
                val marker = MarkerOptions().position(LatLng(location.latitude, location.longitude))
                        .title(it.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                val markerOnMap = view?.addMarkerToMap(marker)
                markerOnMap?.let { locationMarkers.add(markerOnMap) }
            }
        }
    }

    private fun zoomMapAccordingToMarkers() {
        if (locationMarkers.isNotEmpty()) {
            val bounds = markersBounds()
            view?.moveMapCameraToLocation(bounds)
        }
    }

    private fun markersBounds(): LatLngBounds {
        val builder = LatLngBounds.Builder()
        myMarker?.let { builder.include(it.position) }
        for (marker in locationMarkers) {
            builder.include(marker.position)
        }
        return builder.build()
    }

    private fun deleteAllMarkers() {
        locationMarkers.forEach { it.remove() }
        locationMarkers.clear()
    }
}