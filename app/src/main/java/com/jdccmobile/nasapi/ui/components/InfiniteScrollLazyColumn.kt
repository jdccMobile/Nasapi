package com.jdccmobile.nasapi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jdccmobile.nasapi.R
import com.jdccmobile.nasapi.ui.features.home.ErrorUi
import com.jdccmobile.nasapi.ui.features.home.LoadingType
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.theme.Dimens
import kotlinx.collections.immutable.ImmutableList

@Composable
fun InfiniteScrollLazyColumn(
    data: ImmutableList<AstronomicEventUi>,
    onItemClick: (String) -> Unit,
    onLoadMoreItems: () -> Unit,
    isMoreDataLoading: Boolean,
    error: ErrorUi?,
    modifier: Modifier = Modifier,
) {
    val errorLoadingData = error != null && error.type == LoadingType.LoadingMoreData
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.appPadding),
    ) {
        itemsIndexed(data) { index, event ->
            if (index == data.lastIndex && !errorLoadingData) {
                onLoadMoreItems()
            }
            AstronomicEventItem(
                astronomicEventUi = event,
                onClick = onItemClick,
            )
        }
        if (isMoreDataLoading) {
            item { CircularProgressBar(modifier = Modifier.padding(vertical = 16.dp)) }
        }
        if (errorLoadingData) {
            item {
                error?.let {
                    InfoError(
                        errorMessage = it.message,
                        errorIconId = R.drawable.ic_error,
                    )
                }
            }
        }
    }
}
