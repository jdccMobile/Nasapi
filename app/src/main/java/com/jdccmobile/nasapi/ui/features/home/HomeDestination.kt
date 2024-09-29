package com.jdccmobile.nasapi.ui.features.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeDestination(
    navigateToFavorites: () -> Unit,
    navigateToDetails: (String) -> Unit,
) {
    val screenActions = HomeScreenActions(
        navigateToFavorites = navigateToFavorites,
        navigateToDetails = navigateToDetails,
    )
    val viewModel: HomeViewModel = koinViewModel(
        parameters = {
            parametersOf(screenActions)
        },
    )
    HomeScreen(viewModel = viewModel)
}

@Composable
private fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        astronomicEvents = uiState.astronomicEvents.toImmutableList(),
        thereIsFavEvents = uiState.thereIsFavEvents,
        isInitialDataLoading = uiState.isInitialDataLoading,
        isMoreDataLoading = uiState.isMoreDataLoading,
        error = uiState.error,
        onLoadMoreItems = viewModel::onLoadMoreItems,
        navigateToDetails = viewModel::onAstronomicEventClicked,
        navigateToFavorites = viewModel::onFavoritesClicked,
    )
}

@Composable
fun HomeContent(
    astronomicEvents: ImmutableList<AstronomicEventUi>,
    thereIsFavEvents: Boolean,
    isInitialDataLoading: Boolean,
    isMoreDataLoading: Boolean,
    error: ErrorUi?,
    navigateToDetails: (String) -> Unit,
    onLoadMoreItems: () -> Unit,
    navigateToFavorites: () -> Unit,
) {
    TopBarScaffold(
        title = stringResource(R.string.app_name),
        actions = {
            ActionIconButton(
                icon = if (thereIsFavEvents) {
                    Icons.Default.Favorite
                } else {
                    Icons.Outlined.FavoriteBorder
                },
                onClick = {
                    navigateToFavorites()
                },
            )
        },
    ) {
        if (!isInitialDataLoading) {
            if (error?.type != LoadingType.InitialLoading) {
                InfiniteScrollLazyColumn(
                    data = astronomicEvents,
                    onItemClick = navigateToDetails,
                    onLoadMoreItems = onLoadMoreItems,
                    isMoreDataLoading = isMoreDataLoading,
                    error = error,
                )
            } else {
                InfoError(errorMessage = error.message, errorIconId = R.drawable.ic_error)
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
            thereIsFavEvents = false,
            isInitialDataLoading = true,
            error = null,
            navigateToDetails = {},
            onLoadMoreItems = {},
            isMoreDataLoading = false,
            navigateToFavorites = {},
        )
    }
}
