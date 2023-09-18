package com.icyapps.providerapp

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log

class SimpleContentProvider : ContentProvider() {
    private var counterValue = 0

    override fun onCreate(): Boolean {
        Log.d("Provider", "Provider created.")
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        return when (uriMatcher.match(uri)) {
            URI_CODE_COUNTER -> {
                val cursor = MatrixCursor(arrayOf(COUNTER_VALUE_KEY))
                cursor.addRow(arrayOf(counterValue))
                cursor
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        counterValue = values?.getAsInteger(COUNTER_VALUE_KEY) ?: 0
        context?.contentResolver?.notifyChange(uri, null)
        return uri
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    companion object {
        const val URI_PATH = "counter"
        const val COUNTER_VALUE_KEY = "counter_value"
        const val URI_CODE_COUNTER = 1
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI("com.icyapps.providerapp", URI_PATH, URI_CODE_COUNTER)
        }
    }

}