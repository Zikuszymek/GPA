package com.mobile.ziku.gpa.di.modules

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mobile.ziku.gpa.GpaApplication
import com.mobile.ziku.gpa.R
import com.mobile.ziku.gpa.activities.dialogs.SimpleDialog
import com.mobile.ziku.gpa.managers.PermissionManager
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    //ToDo Poprawić Scope

    @Provides
    @Singleton
    fun provideContext(application: GpaApplication): Context = application.applicationContext

    @Provides
    fun provideDisposable() : CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideVerticalLayoutManager(context: Context) : LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    @Provides
    @Singleton
    fun provideLocalizationService(context: Context) : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideSimpleDialog(context: Context) : SimpleDialog = SimpleDialog(context)

    @Provides
    @Named("API_PLACES_KEY")
    @Singleton
    fun provideApiPlacesKey(context: Context):String = context.getString(R.string.google_maps_key)

    @Provides
    @Named("MyLocation")
    @Singleton
    fun provideMyLocation(context: Context):String = context.getString(R.string.my_location)

    @Provides
    @Singleton
    fun providePermissionManager(simpleDialog: SimpleDialog) : PermissionManager = PermissionManager(simpleDialog)
}