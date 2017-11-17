package com.mobile.ziku.gpa.activities.map

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mobile.ziku.gpa.R
import com.mobile.ziku.gpa.activities.BaseActivity
import com.mobile.ziku.gpa.model.PlaceSearched
import com.mobile.ziku.gpa.recyclers.PlaceRecyclerAdapter
import kotlinx.android.synthetic.main.activity_maps.*
import timber.log.Timber
import javax.inject.Inject
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng


class MapsActivity : BaseActivity(), OnMapReadyCallback, MapsContractor.View {

    @Inject lateinit var presenter: MapsContractor.Presenter
    @Inject lateinit var verticalLayputManager: LinearLayoutManager

    private lateinit var mMap: GoogleMap

    private val resultAdapter by lazy { PlaceRecyclerAdapter() }
    private val MyLocalization by lazy { getString(R.string.my_position)}

    private var myMarker: Marker? = null
    private var locationMarkers = mutableListOf<Marker>()
    private var expandedList = false
    private var animationInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = map as SupportMapFragment
        mapFragment.getMapAsync(this)
        initUiComponents()
    }

    private fun initUiComponents() {
        initRecycleView()
        initSearchView()
        initExpandButtonList()
        initMyLocationButton()
    }

    private fun initExpandButtonList() {

    }

    private fun initSearchView() {
        search_view.setOnClickListener {
            search_view.isIconified = false
        }
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                presenter.searchForPlace(query)
                return true
            }
        })
    }

    private fun initRecycleView() {
        search_result_recycler.apply {
            adapter = resultAdapter
            layoutManager = verticalLayputManager
        }
    }

    private fun initMyLocationButton(){
        my_current_location.setOnClickListener {
            presenter.updateCurrentLocalization()
        }
    }

    override fun loadingDataVisibility(visibility: Boolean) {
    }

    override fun updateMyCurrentLocation(location: Location?) {
        if(location != null) {
            val myLocation = LatLng(location.latitude, location.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
            if (myMarker != null) {
                myMarker?.position = myLocation
            } else {
                myMarker = mMap.addMarker(MarkerOptions().position(myLocation).title(MyLocalization))
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLng(myLocation), 2000, null)
        } else {
            myMarker?.remove()
        }
    }


    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.removeView()
    }

    override fun displaySearchResult() {
    }

    override fun displayMessage() {
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    override fun handleSearchResult(placeResponse: List<PlaceSearched>) {
        resultAdapter.placesList = placeResponse.toMutableList()
        resultAdapter.notifyDataSetChanged()
        hideKeyboard()
        handleMarkersUpdate(placeResponse)
    }

    private fun handleMarkersUpdate(placeResponse: List<PlaceSearched>) {
        locationMarkers.forEach { it.remove() }
        locationMarkers.clear()
        placeResponse.forEach {
            val location = it.geometry?.location
            if (location != null && location.lat != null && location.lng != null) {
                val marker = MarkerOptions().position(LatLng(location.lat, location.lng))
                        .title(it.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE) )
                val markerOnMap = mMap.addMarker(marker)
                locationMarkers.add(markerOnMap)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Timber.d("Map is ready")
        mMap = googleMap
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = false
        LocationServices.getFusedLocationProviderClient(this)
    }

    private fun isLocationPermissionEnabled(): Boolean {
        var locationPermissions = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//        locationPermissions.
        return false
    }

}
