package com.jdccmobile.nasapi.ui.features.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jdccmobile.nasapi.ui.components.CardItem
import com.jdccmobile.nasapi.ui.components.TopBarScaffold
import com.jdccmobile.nasapi.ui.theme.Dimens
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import com.jdccmobile.nasapi.ui.theme.lightBlue
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import java.time.LocalDate

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val astronomicalEvents by viewModel.astronomicalEvents.collectAsState()

    HomeContent(
        astronomicEvents = astronomicalEvents,
        onAstronomicEventClicked = viewModel::onAstronomicEventClicked,
        onFavoritesClicked = viewModel::onFavoritesClicked,
    )
}

@Composable
private fun HomeContent(
    astronomicEvents: List<AstronomicEventUi>, // TODO importar immutable list
    onAstronomicEventClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
) {
    TopBarScaffold(
        title = "Nasapi",
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = lightBlue,
                )
            }
        },
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = Dimens.appPadding),
        ) {
            items(astronomicEvents) { event ->
                CardItem(
                    astronomicEventUi = event,
                    onClick = onAstronomicEventClicked,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
            }
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
                    title = "Prueba",
                    description = "Descripcion",
                    date = LocalDate.now(),
                    imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
                ),
            ),
            onAstronomicEventClicked = {},
            onFavoritesClicked = {},
        )
    }
}
