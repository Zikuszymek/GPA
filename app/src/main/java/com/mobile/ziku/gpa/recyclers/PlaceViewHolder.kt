package com.mobile.ziku.gpa.recyclers

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.mobile.ziku.gpa.BR
import com.mobile.ziku.gpa.model.PlaceSearched

class PlaceViewHolder(val itemBinding : ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root){

    fun bindData(place : PlaceSearched){
        itemBinding.setVariable(BR.place, place)
        itemBinding.executePendingBindings()
    }

}