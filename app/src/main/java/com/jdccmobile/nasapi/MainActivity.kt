package com.jdccmobile.nasapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jdccmobile.nasapi.ui.features.home.HomeScreen
import com.jdccmobile.nasapi.ui.theme.NasapiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            NasapiTheme {
                HomeScreen()
            }
        }
    }
}
