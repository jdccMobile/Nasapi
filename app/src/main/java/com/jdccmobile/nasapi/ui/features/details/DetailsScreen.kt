package com.jdccmobile.nasapi.ui.features.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jdccmobile.nasapi.R
import com.jdccmobile.nasapi.ui.components.DetailsScaffold
import com.jdccmobile.nasapi.ui.components.IconAndMessageInfo
import com.jdccmobile.nasapi.ui.components.ImageWithErrorIcon
import com.jdccmobile.nasapi.ui.theme.NasapiTheme

// TODO quitar status bar

@Composable
fun DetailsScreen() {
    DetailsContent()
}

@Suppress("MagicNumber")
@Composable
private fun DetailsContent() {
    val listState = rememberLazyListState()
    val showFab by remember { derivedStateOf { listState.firstVisibleItemScrollOffset == 0 } }

    DetailsScaffold(
        showFab = showFab,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy((-48).dp),
            state = listState,
        ) {
            item {
                ImageWithErrorIcon(
                    imageUrl = "https://apod.nasa.gov/apod/image/2408/M20OriginalLRGBHaO3S2_1500x1100.jpg",
                    modifier = Modifier.height(400.dp),
                )
            }
            item { EventDescription() }
        }
    }
}

@Composable
private fun EventDescription(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            Text(
                text = "Animation: Perseid Meteor Shower",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Text(
                text = "2024-08-10",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            Text(
                text = "But the Trifid Nebula is too faint to be seen by the unaided eye. Over 75 hours of image data captured under dark night skies was used to create this stunning telescopic view.  Watch: The Perseid Meteor Shower",
                style = MaterialTheme.typography.bodySmall,
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

            MyPhotos()
        }
    }
}

@Composable
fun MyPhotos(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.my_photos),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp),
        )
        // TODO hacer comprobacion de si hay fotos
        IconAndMessageInfo(infoText = stringResource(R.string.there_are_no_photos))
    }
}

@Preview
@Composable
private fun HomeScreenDestinationPreview() {
    NasapiTheme {
        DetailsContent()
    }
}
