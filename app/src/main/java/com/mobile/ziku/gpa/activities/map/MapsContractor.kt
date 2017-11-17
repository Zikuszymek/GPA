package com.mobile.ziku.gpa.activities.map

import android.location.Location
import com.mobile.ziku.gpa.activities.BasePresenter
import com.mobile.ziku.gpa.activities.BaseView
import com.mobile.ziku.gpa.model.PlaceSearched
import io.reactivex.Single

interface MapsContractor {

    interface View : BaseView<Presenter>{
        fun displaySearchResult()
        fun displayMessage()
        fun handleSearchResult(placeResponse: List<PlaceSearched>)
        fun loadingDataVisibility(visibility : Boolean)
        fun updateMyCurrentLocation(location: Location?)
    }

    interface Presenter : BasePresenter<View>{
        fun searchForPlace(place : String?)
        fun updateCurrentLocalization()
    }

    interface DataManager{
        fun getDataForPlace(place: String, location: Location) : Single<List<PlaceSearched>>
    }
}