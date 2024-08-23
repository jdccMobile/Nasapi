package com.jdccmobile.nasapi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jdccmobile.nasapi.R

val proportionalFontFamily = FontFamily(
    Font(R.font.proportional_tfb, FontWeight.Normal),
)

val montserratFontFamily = FontFamily(
    Font(R.font.montserrat, FontWeight.Normal),
)

val Typography = Typography(
    titleSmall = TextStyle(
        fontFamily = proportionalFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = proportionalFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
)
