package com.mobile.ziku.gpa.di.modules

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mobile.ziku.gpa.GpaApplication
import com.mobile.ziku.gpa.di.scope.PerActivity
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
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
    fun localizationService(context: Context) : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
}