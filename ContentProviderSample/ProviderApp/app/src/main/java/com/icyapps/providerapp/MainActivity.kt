package com.icyapps.providerapp

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var counter by remember { mutableStateOf(0) }
            val context: Context = LocalContext.current
            Column(Modifier.padding(16.dp)) {
                Button(onClick = {
                    counter++
                    provideIncrementedValue(context, counter)
                }) {
                    Text("Increment and Notify")
                }
                Text("Counter: $counter")
            }
        }
    }

    private fun provideIncrementedValue(context: Context, counterValue: Int) {
        val authority = context.applicationInfo.packageName
        val uri = Uri.parse("content://$authority/${SimpleContentProvider.URI_PATH}")
        val values = ContentValues().apply {
            put(SimpleContentProvider.COUNTER_VALUE_KEY, counterValue)
        }
        contentResolver.insert(uri, values)
    }
}