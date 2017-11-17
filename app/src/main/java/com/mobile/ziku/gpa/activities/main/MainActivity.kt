package com.mobile.ziku.gpa.activities.main

import android.os.Bundle
import com.mobile.ziku.gpa.R
import com.mobile.ziku.gpa.activities.BaseActivity

class MainActivity : BaseActivity(), MainContractor.View{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
