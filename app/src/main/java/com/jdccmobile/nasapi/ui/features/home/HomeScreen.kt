package com.jdccmobile.nasapi.ui.features.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.nasapi.R
import com.jdccmobile.nasapi.ui.components.ActionIconButton
import com.jdccmobile.nasapi.ui.components.CircularProgressBar
import com.jdccmobile.nasapi.ui.components.InfiniteScrollLazyColumn
import com.jdccmobile.nasapi.ui.components.InfoError
import com.jdccmobile.nasapi.ui.components.TopBarScaffold
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import java.time.LocalDate

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    navigateToFavorites: () -> Unit,
    navigateToDetails: (String) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val astronomicalEvents by viewModel.astronomicEvents.collectAsState()
    val isInitialDataLoading by viewModel.isInitialDataLoading.collectAsState()
    val isMoreDataLoading by viewModel.isMoreDataLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    HomeContent(
        astronomicEvents = astronomicalEvents.toImmutableList(),
        isInitialDataLoading = isInitialDataLoading,
        isMoreDataLoading = isMoreDataLoading,
        errorMessage = errorMessage,
//        onAstronomicEventClicked = viewModel::onAstronomicEventClicked,
        onLoadMoreItems = viewModel::onLoadMoreItems,
//        onFavoritesClicked = viewModel::onFavoritesClicked,
        navigateToDetails = navigateToDetails,
        navigateToFavorites = navigateToFavorites,
    )
}

@Composable
private fun HomeContent(
    astronomicEvents: ImmutableList<AstronomicEventUi>,
    isInitialDataLoading: Boolean,
    isMoreDataLoading: Boolean,
    errorMessage: String?,
//    onAstronomicEventClicked: () -> Unit,
    navigateToDetails: (String) -> Unit,
    onLoadMoreItems: () -> Unit,
//    onFavoritesClicked: () -> Unit,
    navigateToFavorites: () -> Unit,
) {
    TopBarScaffold(
        title = stringResource(R.string.app_name),
        actions = {
            ActionIconButton(
                icon = Icons.Default.Favorite,
                onClick = {
                    navigateToFavorites()
//                    onFavoritesClicked()
                },
            )
        },
    ) {
        if (!isInitialDataLoading) {
            if (errorMessage.isNullOrEmpty()) {
                InfiniteScrollLazyColumn(
                    data = astronomicEvents,
                    onItemClick = navigateToDetails,
                    onLoadMoreItems = onLoadMoreItems,
                    isMoreDataLoading = isMoreDataLoading,
                )
            } else {
                InfoError(errorMessage = errorMessage, errorIconId = R.drawable.ic_error)
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
        HomeContent(
            astronomicEvents = listOf(
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
            isInitialDataLoading = true,
            errorMessage = null,
            navigateToDetails = {},
            onLoadMoreItems = {},
            isMoreDataLoading = false,
            navigateToFavorites = {},
//            onFavoritesClicked = {},
        )
    }
}
