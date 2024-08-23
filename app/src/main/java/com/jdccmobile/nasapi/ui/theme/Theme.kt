package com.jdccmobile.nasapi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorScheme = darkColorScheme(
    primary = lightBlue,
    secondary = bluishGray,
    tertiary = purple,
    surface = darkBlue,
    onSurface = Color.White,
    background = bluishBlack,
    onBackground = Color.White,
)

@Composable
fun NasapiTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        shapes = shapes,
        content = content,
    )
}
