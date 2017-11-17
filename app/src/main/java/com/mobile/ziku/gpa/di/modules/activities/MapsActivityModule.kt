package com.mobile.ziku.gpa.di.modules.activities

import com.mobile.ziku.gpa.activities.map.MapsContractor
import com.mobile.ziku.gpa.activities.map.MapsDataManager
import com.mobile.ziku.gpa.activities.map.MapsPresenter
import com.mobile.ziku.gpa.di.scope.PerActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MapsActivityModule {

    @Binds
    @PerActivity
    abstract fun bindMapsPresenter(mapsPresenter: MapsPresenter) : MapsContractor.Presenter

    @Binds
    @PerActivity
    abstract fun bindMapsDataManager(mapsDataManager: MapsDataManager) : MapsContractor.DataManager
}