package com.mobile.ziku.gpa

import android.support.test.espresso.Espresso.closeSoftKeyboard
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mobile.ziku.gpa.activities.map.MapsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.pressImeActionButton
import android.support.test.espresso.action.ViewActions.typeText
import android.widget.EditText


@RunWith(AndroidJUnit4::class)
class MapActivityTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<MapsActivity>(MapsActivity::class.java)

    @Test
    fun typeSearchTest() {
        onView(withId(R.id.my_current_location)).perform(click())
        onView(withId(R.id.search_view)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("No such item"), pressImeActionButton())
    }
}