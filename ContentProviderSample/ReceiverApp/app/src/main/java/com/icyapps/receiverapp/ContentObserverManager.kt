package com.icyapps.receiverapp

import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class ContentObserverManager(private val activity: ComponentActivity) {
    private lateinit var contentObserver: ContentObserver

    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            registerContentObserver()
        } else {
            Log.e(TAG, "Permission denied.")
        }
    }

    fun initializeContentObserver(onValueChange: () -> Unit) {
        // Initialize ContentObserver
        contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                Log.d(TAG, "Value changed")
                onValueChange()
            }
        }

        val readPermission = CONTENT_PROVIDER_PERMISSION
        if (ContextCompat.checkSelfPermission(
                activity,
                readPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            registerContentObserver()
        } else {
            requestPermissionLauncher.launch(readPermission)
        }
    }

    private fun registerContentObserver() {
        Log.d(TAG, "registerContentObserver")
        try {
            activity.contentResolver.registerContentObserver(
                Uri.parse("content://$AUTHORITY/$URI_PATH"),
                true,
                contentObserver
            )
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to register ContentObserver", e)
        }
    }

    fun onDestroy() {
        Log.d(TAG, "unregisterContentObserver")
        activity.contentResolver.unregisterContentObserver(contentObserver)
    }

    companion object {
        const val TAG = "ContentObserverManager"
        const val AUTHORITY = "com.icyapps.providerapp"
        const val URI_PATH = "counter"
        const val COUNTER_VALUE_KEY = "counter_value"
        const val CONTENT_PROVIDER_PERMISSION = "com.icyapps.providerapp.permission.READ_DATA"

    }
}