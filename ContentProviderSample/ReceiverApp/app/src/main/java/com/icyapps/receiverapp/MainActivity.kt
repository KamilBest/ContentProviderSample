package com.icyapps.receiverapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private lateinit var contentObserverManager: ContentObserverManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentObserverManager = ContentObserverManager(this)

        setContent {
            var counter by remember { mutableStateOf(0) }
            contentObserverManager.initializeContentObserver(onValueChange = {
                counter = updateCounterFromProvider()
            })


            // Initial fetch
            LaunchedEffect(Unit) {
                counter = updateCounterFromProvider()
            }

            Column(Modifier.padding(16.dp)) {
                Text("Received Counter: $counter")
            }
        }
    }

    private fun updateCounterFromProvider(): Int {
        val cursor = contentResolver.query(
            Uri.parse("content://${ContentObserverManager.AUTHORITY}/${ContentObserverManager.URI_PATH}"),
            null, null, null, null
        )
        var counter = -1
        cursor?.apply {
            moveToFirst()
            counter = getInt(getColumnIndexOrThrow(ContentObserverManager.COUNTER_VALUE_KEY))
            close()
        }
        return counter
    }

    override fun onDestroy() {
        contentObserverManager.onDestroy()
        super.onDestroy()
    }
}