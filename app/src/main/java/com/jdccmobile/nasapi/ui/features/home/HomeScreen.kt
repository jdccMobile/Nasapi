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
import java.time.LocalDate

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    navigateToFavorites: () -> Unit,
    navigateToDetails: (String) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val astronomicalEvents by viewModel.astronomicEvents.collectAsStateWithLifecycle()
    val thereIsFavEvents by viewModel.thereIsFavEvents.collectAsStateWithLifecycle()
    val isInitialDataLoading by viewModel.isInitialDataLoading.collectAsStateWithLifecycle()
    val isMoreDataLoading by viewModel.isMoreDataLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    HomeContent(
        astronomicEvents = astronomicalEvents.toImmutableList(),
        thereIsFavEvents = thereIsFavEvents,
        isInitialDataLoading = isInitialDataLoading,
        isMoreDataLoading = isMoreDataLoading,
        errorMessage = errorMessage,
        onLoadMoreItems = viewModel::onLoadMoreItems,
        navigateToDetails = navigateToDetails,
        navigateToFavorites = navigateToFavorites,
    )
}

@Composable
private fun HomeContent(
    astronomicEvents: ImmutableList<AstronomicEventUi>,
    thereIsFavEvents: Boolean,
    isInitialDataLoading: Boolean,
    isMoreDataLoading: Boolean,
    errorMessage: String?,
    navigateToDetails: (String) -> Unit,
    onLoadMoreItems: () -> Unit,
    navigateToFavorites: () -> Unit,
) {
    TopBarScaffold(
        title = stringResource(R.string.app_name),
        actions = {
            ActionIconButton(
                icon = if(thereIsFavEvents) {
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
            thereIsFavEvents = false,
            isInitialDataLoading = true,
            errorMessage = null,
            navigateToDetails = {},
            onLoadMoreItems = {},
            isMoreDataLoading = false,
            navigateToFavorites = {},
        )
    }
}
