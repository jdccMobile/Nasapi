package com.jdccmobile.nasapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jdccmobile.nasapi.ui.features.favorites.FavoritesScreen
import com.jdccmobile.nasapi.ui.theme.NasapiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            NasapiTheme {
                FavoritesScreen()
            }
        }
    }
}
