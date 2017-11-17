package com.mobile.ziku.gpa.activities.map

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapsPresenter @Inject constructor(
        val compositeDisposable: CompositeDisposable,
        val dataManager: MapsDataManager,
        val fusedLocationProviderClient: FusedLocationProviderClient
) : MapsContractor.Presenter {

    var view: MapsContractor.View? = null

    override fun attachView(view: MapsContractor.View) {
        this.view = view
    }

    override fun removeView() {
        view = null
    }

    private fun checkLocationPermission(){

    }

    override fun updateCurrentLocalization() {
        fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { view?.updateMyCurrentLocation(it) }
                .addOnFailureListener { view?.displayMessage() }
    }

    override fun searchForPlace(place: String?) {
        fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { searchForQuery(place, it) }
                .addOnFailureListener { view?.displayMessage() }
    }

    private fun searchForQuery(place: String?, location : Location){
        //ToDo remove property
        if (place != null)
            place == ""

        compositeDisposable.add(
                dataManager.getDataForPlace(place!!, location)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            view?.handleSearchResult(it)
                        }, {
                            view?.displayMessage()
                        })
        )
    }
}