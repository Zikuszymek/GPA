package com.mobile.ziku.gpa.activities.dialogs

import android.content.Context
import android.support.v7.app.AlertDialog
import com.mobile.ziku.gpa.R
import javax.inject.Inject

class SimpleDialog @Inject constructor(
        val context: Context
) {

    fun showDialog(message: String) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(context.getString(R.string.app_name))
        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", { _, _ ->  alertDialog.dismiss() })
        alertDialog.show()
    }

    fun showLocationPermissionDialog() {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(context.getString(R.string.app_name))
        alertDialog.setMessage("Elsodgo")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", { _, _ ->  alertDialog.dismiss() })
        alertDialog.show()
    }

}