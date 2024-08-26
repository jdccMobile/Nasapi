package com.jdccmobile.nasapi.ui.features.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.jdccmobile.nasapi.ui.components.CardItem
import com.jdccmobile.nasapi.ui.components.TopBarScaffold
import com.jdccmobile.nasapi.ui.theme.Dimens
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import com.jdccmobile.nasapi.ui.theme.lightBlue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import java.time.LocalDate

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val astronomicalEvents by viewModel.astronomicalEvents.collectAsState()
    val isDataLoaded by viewModel.isDataLoaded.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    HomeContent(
        astronomicEvents = astronomicalEvents.toImmutableList(),
        isDataLoaded = isDataLoaded,
        errorMessage = errorMessage,
        onAstronomicEventClicked = viewModel::onAstronomicEventClicked,
//        onFavoritesClicked = viewModel::onFavoritesClicked,
    )
}

@Composable
private fun HomeContent(
    // TODO importar immutable list
    astronomicEvents: ImmutableList<AstronomicEventUi>,
    isDataLoaded: Boolean,
    errorMessage: String?,
    onAstronomicEventClicked: () -> Unit,
//    onFavoritesClicked: () -> Unit,
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
        if (isDataLoaded) {
            if (errorMessage.isNullOrEmpty()) {
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
            } else {
                Log.i("asd", errorMessage)
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
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center),
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
            ).toImmutableList(),
            isDataLoaded = true,
            errorMessage = null,
            onAstronomicEventClicked = {},
//            onFavoritesClicked = {},
        )
    }
}
