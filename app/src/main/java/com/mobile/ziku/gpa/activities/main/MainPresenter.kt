package com.mobile.ziku.gpa.activities.main

import javax.inject.Inject

class MainPresenter @Inject constructor(
): MainContractor.Presenter{

    var view : MainContractor.View? = null

    override fun removeView() {
        this.view = null
    }

    override fun attachView(view: MainContractor.View) {
        this.view = view
    }
}