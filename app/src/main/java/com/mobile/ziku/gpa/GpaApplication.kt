package com.mobile.ziku.gpa

import android.content.Context
import com.mobile.ziku.gpa.di.components.DaggerGpaComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

class GpaApplication : DaggerApplication(){

    companion object {

        operator fun get(context: Context): GpaApplication {
            return context.applicationContext as GpaApplication
        }

        @JvmStatic
        @BindingAdapter("app:srcCompat")
        fun setImageUrl(view: ImageView, url: String) {
            Glide.with(view.getContext()).load(url).into(view)
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val gpaComponent = DaggerGpaComponent.builder()
                .application(this)
                .build()
        gpaComponent.inject(this)
        return gpaComponent
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}