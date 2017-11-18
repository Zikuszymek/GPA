package com.mobile.ziku.gpa.managers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.mobile.ziku.gpa.activities.dialogs.SimpleDialog
import javax.inject.Inject

class PermissionManager @Inject constructor(
        val simpleDialog: SimpleDialog
) {

    private var actionAwaits: (() -> Unit)? = null

    companion object {
        const val FINE_LOCATION_PERMISSION = 1
    }

    fun isLocationPermissionEnabled(activity: Activity, awaitinFunction: () -> Unit): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                simpleDialog.showLocationPermissionDialog()
                actionAwaits = null
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION_PERMISSION)
                actionAwaits = awaitinFunction
            }
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
                    actionAwaits = null
                }
                return
            }
        }
    }
}

