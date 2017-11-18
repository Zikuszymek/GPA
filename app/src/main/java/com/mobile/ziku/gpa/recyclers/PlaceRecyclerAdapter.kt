package com.mobile.ziku.gpa.recyclers

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.mobile.ziku.gpa.R
import com.mobile.ziku.gpa.model.PlaceSearched

class PlaceRecyclerAdapter(val moveCameraToLocation : (LatLng, Float) -> Unit) : RecyclerView.Adapter<PlaceViewHolder>() {

    var placesList = mutableListOf<PlaceSearched>()

    override fun onBindViewHolder(holder: PlaceViewHolder?, position: Int) {
        holder?.bindData(placesList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(inflater,R.layout.place_view_holder,parent,false)
        return PlaceViewHolder(viewDataBinding, moveCameraToLocation)
    }

    override fun getItemCount() = placesList.size

}