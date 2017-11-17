package com.mobile.ziku.gpa.di.modules

import com.mobile.ziku.gpa.activities.map.MapsActivity
import com.mobile.ziku.gpa.di.modules.activities.MapsActivityModule
import com.mobile.ziku.gpa.di.scope.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AcitivitesModule {

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(MapsActivityModule::class))
    abstract fun provideMapsActivity(): MapsActivity
}