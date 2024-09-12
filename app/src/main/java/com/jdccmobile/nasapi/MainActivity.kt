package com.jdccmobile.nasapi

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jdccmobile.nasapi.ui.features.details.DetailsScreen
import com.jdccmobile.nasapi.ui.theme.NasapiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(this, CAMERAX_PERMISSIONS, 0)
        }
        super.onCreate(savedInstanceState)
        setContent {
            NasapiTheme {
                DetailsScreen()
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean =
        CAMERAX_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                permission,
            ) == PackageManager.PERMISSION_GRANTED
        }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
