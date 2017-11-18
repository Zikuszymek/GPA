package com.mobile.ziku.gpa.activities.map

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.transition.TransitionManager
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.mobile.ziku.gpa.R
import com.mobile.ziku.gpa.activities.BaseActivity
import com.mobile.ziku.gpa.model.PlaceSearched
import com.mobile.ziku.gpa.recyclers.PlaceRecyclerAdapter
import kotlinx.android.synthetic.main.activity_maps.*
import javax.inject.Inject
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.maps.model.LatLng
import com.mobile.ziku.gpa.activities.dialogs.SimpleDialog
import com.mobile.ziku.gpa.enums.MessageType
import com.mobile.ziku.gpa.managers.PermissionManager

class MapsActivity : BaseActivity(), OnMapReadyCallback, MapsContractor.View {

    @Inject lateinit var presenter: MapsContractor.Presenter
    @Inject lateinit var verticalLayoutManager: LinearLayoutManager
    @Inject lateinit var simpleDialog: SimpleDialog
    @Inject lateinit var permissionManager: PermissionManager

    private var googleMap: GoogleMap? = null
    private val resultAdapter by lazy { PlaceRecyclerAdapter(this::moveCameraToLocation) }
    private var listIsExpanded = false

    companion object {
        const val CITY_ZOOM = 15f
        const val PLACE_ZOOM = 18f
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        enableMyLocalizationOnMap()
    }

    @SuppressWarnings("MissingPermission")
    private fun enableMyLocalizationOnMap() {
        if (permissionManager.isLocationPermissionEnabled(this) { enableMyLocalizationOnMap() }) {
            this.googleMap?.isMyLocationEnabled = true
            this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false
            presenter.updateCurrentLocalization()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
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
        expandable_button.setOnClickListener {
            beginDelayedTransition()
            if (listIsExpanded) {
                collapseRecyclerView()
            } else {
                expandRecyclerView()
            }
        }
    }

    private fun expandRecyclerView(){
        listIsExpanded = true
        beginDelayedTransition()
        search_result_recycler.visibility = View.VISIBLE
    }

    private fun collapseRecyclerView(){
        listIsExpanded = false
        beginDelayedTransition()
        search_result_recycler.visibility = View.GONE
    }

    private fun initSearchView() {
        search_view.apply {
            queryHint = getString(R.string.search_hint)
            setOnClickListener { search_view.isIconified = false }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null && !TextUtils.isEmpty(query)) {
                        hideKeyboard()
                        searchForPhrase(query)
                    }
                    return true
                }
            })
        }
    }

    private fun searchForPhrase(query: String) {
        if (permissionManager.isLocationPermissionEnabled(this) { searchForPhrase(query) }) {
            progressBarVisibility(true)
            presenter.searchForPlace(query)
        }
    }

    private fun initRecycleView() {
        search_result_recycler.apply {
            adapter = resultAdapter
            layoutManager = verticalLayoutManager
        }
    }

    private fun initMyLocationButton() {
        my_current_location.setOnClickListener {
            if (permissionManager.isLocationPermissionEnabled(this) { presenter.updateCurrentLocalization() })
                presenter.updateCurrentLocalization()
        }
    }

    override fun onBackPressed() {
        hideKeyboard()
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.removeView()
    }

    override fun displayMessage(messageType: MessageType) {
        val messageString = when (messageType) {
            MessageType.LOCALIZATION_ERROR -> R.string.localization_error
            MessageType.NO_RESULT -> R.string.no_search_result
            MessageType.SEARCH_PROBLEM -> R.string.search_result_problem
        }
        simpleDialog.showDialog(getString(messageString))
    }

    override fun handleSearchResult(placeResponse: List<PlaceSearched>) {
        progressBarVisibility(false)
        expandRecyclerView()
        resultAdapter.placesList = placeResponse.toMutableList()
        resultAdapter.notifyDataSetChanged()
        hideKeyboard()
    }

    override fun moveCameraToLocation(latLng: LatLng, zoomLevel: Float) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel), 1000, null)
    }

    override fun addMarker(markerOptions: MarkerOptions) = googleMap?.addMarker(markerOptions)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun progressBarVisibility(visibility: Boolean) {
        beginDelayedTransition()
        when (visibility) {
            true -> progress_bar.visibility = View.VISIBLE
            false -> progress_bar.visibility = View.GONE
        }
    }

    private fun beginDelayedTransition() {
        TransitionManager.beginDelayedTransition(findViewById(android.R.id.content))
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

}
