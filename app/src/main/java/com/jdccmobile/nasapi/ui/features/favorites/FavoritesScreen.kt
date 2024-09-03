package com.jdccmobile.nasapi.ui.features.favorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jdccmobile.nasapi.R
import com.jdccmobile.nasapi.ui.components.TopBarWithNavigationScaffold
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel = koinViewModel()) {

    FavoritesContent(
    )
}

@Composable
private fun FavoritesContent(
) {
    TopBarWithNavigationScaffold(
        title = stringResource(R.string.favorites),
    ) {
    }
}

@Preview
@Composable
private fun HomeScreenDestinationPreview() {
    NasapiTheme {
        FavoritesContent(
        )
    }
}
