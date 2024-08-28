package com.jdccmobile.nasapi.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jdccmobile.nasapi.R
import com.jdccmobile.nasapi.ui.components.ActionIconButton
import com.jdccmobile.nasapi.ui.components.CardItem
import com.jdccmobile.nasapi.ui.components.CircularProgressBar
import com.jdccmobile.nasapi.ui.components.TopBarScaffold
import com.jdccmobile.nasapi.ui.theme.Dimens
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import java.time.LocalDate

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val astronomicalEvents by viewModel.astronomicalEvents.collectAsState()
    val isInitialDataLoading by viewModel.isInitialDataLoading.collectAsState()
    val isMoreDataLoading by viewModel.isMoreDataLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    HomeContent(
        astronomicEvents = astronomicalEvents.toImmutableList(),
        isInitialDataLoading = isInitialDataLoading,
        isMoreDataLoading = isMoreDataLoading,
        errorMessage = errorMessage,
        onAstronomicEventClicked = viewModel::onAstronomicEventClicked,
        onLoadMoreItems = viewModel::onLoadMoreItems,
//        onFavoritesClicked = viewModel::onFavoritesClicked,
    )
}

@Composable
private fun HomeContent(
    astronomicEvents: ImmutableList<AstronomicEventUi>,
    isInitialDataLoading: Boolean,
    isMoreDataLoading: Boolean,
    errorMessage: String?,
    onAstronomicEventClicked: () -> Unit,
    onLoadMoreItems: () -> Unit,
//    onFavoritesClicked: () -> Unit,
) {
    TopBarScaffold(
        title = "Nasapi",
        actions = {
            ActionIconButton(
                icon = Icons.Default.Favorite,
                onClick = {
                    // TODO navigate to favorites
                },
            )
        },
    ) {
        if (!isInitialDataLoading) {
            if (errorMessage.isNullOrEmpty()) {
                InfiniteScrollList(
                    astronomicEvents = astronomicEvents,
                    onAstronomicEventClicked = onAstronomicEventClicked,
                    onLoadMoreItems = onLoadMoreItems,
                    isMoreDataLoading = isMoreDataLoading,
                )
            } else {
                InfoError(errorMessage = errorMessage)
            }
        } else {
            CircularProgressBar()
        }
    }
}

@Composable
private fun InfiniteScrollList(
    astronomicEvents: ImmutableList<AstronomicEventUi>,
    onAstronomicEventClicked: () -> Unit,
    onLoadMoreItems: () -> Unit,
    isMoreDataLoading: Boolean,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = Dimens.appPadding),
    ) {
        itemsIndexed(astronomicEvents) { index, event ->
            if (index == astronomicEvents.lastIndex)
                {
                    onLoadMoreItems()
                }
            CardItem(
                astronomicEventUi = event,
                onClick = onAstronomicEventClicked,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
        if (isMoreDataLoading)
            {
                item { CircularProgressBar(modifier = Modifier.padding(vertical = 16.dp)) }
            }
    }
}

@Composable
private fun InfoError(errorMessage: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(104.dp),
        )
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Preview
@Composable
private fun HomeScreenDestinationPreview() {
    NasapiTheme {
        HomeContent(
            astronomicEvents = listOf(
                AstronomicEventUi(
                    title = "Prueba",
                    description = "Descripcion",
                    date = LocalDate.now(),
                    imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
                ),
            ).toImmutableList(),
            isInitialDataLoading = true,
            errorMessage = null,
            onAstronomicEventClicked = {},
            onLoadMoreItems = {},
            isMoreDataLoading = false,
//            onFavoritesClicked = {},
        )
    }
}
