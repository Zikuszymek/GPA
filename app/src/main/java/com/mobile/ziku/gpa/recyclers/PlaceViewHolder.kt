package com.mobile.ziku.gpa.recyclers

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.mobile.ziku.gpa.BR
import com.mobile.ziku.gpa.activities.map.MapsActivity.Companion.PLACE_ZOOM
import com.mobile.ziku.gpa.model.PlaceSearched

class PlaceViewHolder(val itemBinding : ViewDataBinding, val moveCameraToLocation : (LatLng, Float) -> Unit) : RecyclerView.ViewHolder(itemBinding.root){

    fun bindData(place : PlaceSearched){
        itemBinding.setVariable(BR.place, place)
        itemBinding.executePendingBindings()
        itemBinding.root.setOnClickListener {
            val placeLocation = place.geometry?.location
            if (placeLocation?.longitude != null) {
                val cameraLocation = LatLng(placeLocation.latitude, placeLocation.longitude)
                moveCameraToLocation.invoke(cameraLocation, PLACE_ZOOM)
            }
        }
    }

}