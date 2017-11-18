package com.mobile.ziku.gpa.managers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.mobile.ziku.gpa.activities.dialogs.SimpleDialog
import javax.inject.Inject

class PermissionManager @Inject constructor(
        val simpleDialog: SimpleDialog
) {

    private var actionAwaits: (() -> Unit)? = null
    private var currentActivity : Activity? = null

    companion object {
        const val FINE_LOCATION_PERMISSION = 1
    }

    fun isLocationPermissionEnabled(activity: Activity, awaitingFunction: () -> Unit): Boolean {
        currentActivity = activity
        val fineLocationPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION_PERMISSION)
                actionAwaits = awaitingFunction
            return false
        } else {
            actionAwaits = null
            return true
        }
    }


    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            FINE_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == (PackageManager.PERMISSION_GRANTED)) {
                    actionAwaits?.invoke()
                } else {
                    simpleDialog.showLocationPermissionDialog(currentActivity as Context)
                }
                actionAwaits = null
                return
            }
        }
    }
}

