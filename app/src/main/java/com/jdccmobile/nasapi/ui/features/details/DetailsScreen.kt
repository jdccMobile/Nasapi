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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.nasapi.R
import com.jdccmobile.nasapi.ui.components.CircularProgressBar
import com.jdccmobile.nasapi.ui.components.DetailsScaffold
import com.jdccmobile.nasapi.ui.components.IconAndMessageInfo
import com.jdccmobile.nasapi.ui.components.ImageWithErrorIcon
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import java.time.LocalDate

@OptIn(KoinExperimentalAPI::class)
@Composable
fun DetailsScreen(viewModel: DetailsViewModel = koinViewModel()) {
    val astronomicEvent by viewModel.astronomicEvent.collectAsState()
    val isDataLoading by viewModel.isDataLoading.collectAsState()
    DetailsContent(
        astronomicEvent = astronomicEvent,
        isDataLoading = isDataLoading,
        onFavoriteFabClicked = viewModel::onFavoriteFabClicked,
        onTakePhotoFabClicked = viewModel::onTakePhotoFabClicked,
    )
}

@Suppress("MagicNumber")
@Composable
private fun DetailsContent(
    astronomicEvent: AstronomicEventUi?,
    isDataLoading: Boolean,
    onFavoriteFabClicked: () -> Unit,
    onTakePhotoFabClicked: () -> Unit,
) {
    val listState = rememberLazyListState()
    val showFab by remember { derivedStateOf { listState.firstVisibleItemScrollOffset == 0 } }
    val favoriteFabIcon = if (astronomicEvent?.isFavorite == true) {
        Icons.Outlined.Favorite
    } else {
        Icons.Outlined.FavoriteBorder
    }

    DetailsScaffold(
        showFab = showFab,
        favoriteFabIcon = favoriteFabIcon,
        onFavoriteFabClicked = onFavoriteFabClicked,
        onTakePhotoFabClicked = onTakePhotoFabClicked,
    ) {
        if (isDataLoading) {
            CircularProgressBar()
        } else {
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
                item {
                    astronomicEvent?.let {
                        EventDescription(
                            astronomicEvent = it,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventDescription(
    astronomicEvent: AstronomicEventUi,
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
                text = astronomicEvent.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Text(
                text = astronomicEvent.date.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            Text(
                text = astronomicEvent.description,
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
        DetailsContent(
            astronomicEvent = AstronomicEventUi(
                id = AstronomicEventId("1"),
                title = "Prueba",
                description = "Descripcion",
                date = LocalDate.now(),
                imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
                isFavorite = false,
                hasImage = false,
            ),
            isDataLoading = false,
            onFavoriteFabClicked = {},
            onTakePhotoFabClicked = {},
        )
    }
}
