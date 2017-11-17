package com.mobile.ziku.gpa.http

import com.mobile.ziku.gpa.model.PlacesResponse
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

class RetrofitService {

    interface PlacesService {
        @POST("nearbysearch/json?")
        fun searchPlacesByName(@Query("location") location: String, @Query("radius") radius: Int,
                               @Query("name") name: String, @Query("key") key: String) : Single<PlacesResponse>
    }
}