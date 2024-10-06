package com.jdccmobile.nasapi.ui.utils

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.debugBorder(color: Color = Color.Magenta) = this.border(width = 1.dp, color = color)
