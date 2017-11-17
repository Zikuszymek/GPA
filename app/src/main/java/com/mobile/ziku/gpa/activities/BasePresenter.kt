package com.mobile.ziku.gpa.activities

interface BasePresenter<T> {
    fun attachView(view : T)
    fun removeView()
}