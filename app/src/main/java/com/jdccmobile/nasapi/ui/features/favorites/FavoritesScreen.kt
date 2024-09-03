package com.jdccmobile.nasapi.ui.features.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.nasapi.R
import com.jdccmobile.nasapi.ui.components.CardItem
import com.jdccmobile.nasapi.ui.components.CircularProgressBar
import com.jdccmobile.nasapi.ui.components.TopBarWithNavigationScaffold
import com.jdccmobile.nasapi.ui.features.home.AstronomicEventUi
import com.jdccmobile.nasapi.ui.theme.Dimens
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import java.time.LocalDate

@OptIn(KoinExperimentalAPI::class)
@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel = koinViewModel()) {
    val favoriteEvents by viewModel.favoriteEvents.collectAsState()
    val isInitialDataLoading by viewModel.isInitialDataLoading.collectAsState()
    FavoritesContent(
        favoriteEvents = favoriteEvents.toImmutableList(),
        isInitialDataLoading = isInitialDataLoading,
        onFavoriteEventClicked = viewModel::onFaoriteEventClicked,
    )
}

@Composable
private fun FavoritesContent(
    favoriteEvents: ImmutableList<AstronomicEventUi>,
    isInitialDataLoading: Boolean,
    onFavoriteEventClicked: () -> Unit,
) {
    TopBarWithNavigationScaffold(
        title = stringResource(R.string.favorites),
    ) {
        if (!isInitialDataLoading) {
            // TODO añadir info sobre error con un flow
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.appPadding),
            ) {
                items(favoriteEvents) { event ->
                    CardItem(
                        astronomicEventUi = event,
                        onClick = { onFavoriteEventClicked() },
                    )
                }
            }
        } else {
            CircularProgressBar()
        }
    }
}

@Preview
@Composable
private fun HomeScreenDestinationPreview() {
    NasapiTheme {
        FavoritesContent(
            favoriteEvents = listOf(
                AstronomicEventUi(
                    id = AstronomicEventId("1"),
                    title = "Prueba",
                    description = "Descripcion",
                    date = LocalDate.now(),
                    imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
                    isFavorite = false,
                    hasImage = false,
                ),
            ).toImmutableList(),
            isInitialDataLoading = false,
            onFavoriteEventClicked = {},
        )
    }
}
