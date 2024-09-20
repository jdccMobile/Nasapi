package com.jdccmobile.nasapi.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

sealed class IconResourceType {
    data class ImageVector(val imageVector: androidx.compose.ui.graphics.vector.ImageVector) :
        IconResourceType()

    data class Drawable(
        @DrawableRes val drawableId: Int,
    ) : IconResourceType()
}

@Composable
fun IconResourceTypeContent(icon: IconResourceType) {
    when (icon) {
        is IconResourceType.ImageVector -> {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .padding(4.dp),
            )
        }

        is IconResourceType.Drawable -> {
            Icon(
                painter = painterResource(id = icon.drawableId),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .padding(4.dp),
            )
        }
    }
}
