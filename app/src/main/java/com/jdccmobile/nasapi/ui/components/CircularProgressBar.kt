package com.jdccmobile.nasapi.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(LOADING_INDICATOR_TAG),
    ) {
        CircularProgressIndicator(
            modifier = modifier
                .size(48.dp)
                .align(Alignment.Center),
        )
    }
}

const val LOADING_INDICATOR_TAG = "loadingIndicator"
