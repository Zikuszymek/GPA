package com.mobile.ziku.gpa.activities.main

import com.mobile.ziku.gpa.activities.BasePresenter
import com.mobile.ziku.gpa.activities.BaseView


interface MainContractor {

    interface View : BaseView<Presenter>{

    }

    interface Presenter : BasePresenter<View>{

    }
}