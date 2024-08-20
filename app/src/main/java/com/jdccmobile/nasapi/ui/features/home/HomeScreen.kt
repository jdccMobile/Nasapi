package com.jdccmobile.nasapi.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jdccmobile.nasapi.ui.components.CardItem
import com.jdccmobile.nasapi.ui.components.TopBarScaffold
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import com.jdccmobile.nasapi.ui.theme.cardAccent
import java.time.LocalDate

@Composable
fun HomeScreen(
) {
    // TODO llamar viewmodels
    HomeContent(
        astronomicEvents = eventsMock,
        onAstronomicEventClicked = {},
        onFavoritesClicked = {},
    )
}

@Composable
private fun HomeContent(
    astronomicEvents: List<AstronomicEventUi>, // TODO importar immutable list
    onAstronomicEventClicked: (String) -> Unit,
    onFavoritesClicked: () -> Unit,
) {
    TopBarScaffold(
        title = "Nasapi",
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = cardAccent,
                )
            }
        },
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(astronomicEvents) { event ->
                CardItem(
                    title = event.title,
                    date = event.date.toString(),
                    imageUrl = event.imageUrl,
                    onClick = onAstronomicEventClicked,
                )
            }
        }
    }
}

data class AstronomicEventUi(
    // TODO asd llevar al vm
    val title: String,
    val description: String,
    val date: LocalDate, // Mirar tranformaciones de todate y totime
    val imageUrl: String,
)

@Preview
@Composable
private fun HomeScreenDestinationPreview() {
    NasapiTheme {
        HomeContent(
            astronomicEvents = eventsMock,
            onAstronomicEventClicked = {},
            onFavoritesClicked = {},
        )
    }
}

private val eventsMock = listOf(
    AstronomicEventUi(
        title = "Prueba",
        description = "Descripcion",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
    ),
    AstronomicEventUi(
        title = "Prueba 2",
        description = "Descripcion",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/GloryFog_label.png",
    ),
    AstronomicEventUi(
        title = "Prueba 3",
        description = "Descripcion",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/Rhemann799_109P_24_11_92.jpg",
    ),
)