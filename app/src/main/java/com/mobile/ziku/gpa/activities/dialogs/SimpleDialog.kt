package com.mobile.ziku.gpa.activities.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import com.mobile.ziku.gpa.R
import javax.inject.Inject

class SimpleDialog @Inject constructor() {

    fun showDialog(message: String, context: Context) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(context.getString(R.string.app_name))
        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.ok), { _, _ -> alertDialog.dismiss() })
        alertDialog.show()
    }

    fun showLocationPermissionDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(context.getString(R.string.app_name))
        alertDialog.setMessage(context.getString(R.string.request_location_permission))
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.ok), { _, _ -> alertDialog.dismiss() })
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.settings), { _, _ -> startSettingsActivity(context) })
        alertDialog.show()
    }

    private fun startSettingsActivity(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}