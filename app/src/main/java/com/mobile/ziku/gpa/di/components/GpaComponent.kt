package com.mobile.ziku.gpa.di.components

import com.mobile.ziku.gpa.GpaApplication
import com.mobile.ziku.gpa.di.modules.AcitivitesModule
import com.mobile.ziku.gpa.di.modules.AppModule
import com.mobile.ziku.gpa.di.modules.HttpModules
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AcitivitesModule::class,
        HttpModules::class,
        AppModule::class
))
interface GpaComponent : AndroidInjector<GpaApplication>{

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: GpaApplication) : GpaComponent.Builder
        fun build() : GpaComponent
    }
}