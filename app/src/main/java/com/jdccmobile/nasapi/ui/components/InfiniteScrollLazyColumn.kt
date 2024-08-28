package com.jdccmobile.nasapi.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jdccmobile.nasapi.ui.features.home.AstronomicEventUi
import com.jdccmobile.nasapi.ui.theme.Dimens
import kotlinx.collections.immutable.ImmutableList

@Composable
fun InfiniteScrollLazyColumn(
    data: ImmutableList<AstronomicEventUi>,
    onItemClick: () -> Unit,
    onLoadMoreItems: () -> Unit,
    isMoreDataLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.appPadding),
    ) {
        itemsIndexed(data) { index, event ->
            if (index == data.lastIndex) {
                onLoadMoreItems()
            }
            CardItem(
                astronomicEventUi = event,
                onClick = onItemClick,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
        if (isMoreDataLoading) {
            item { CircularProgressBar(modifier = Modifier.padding(vertical = 16.dp)) }
        }
    }
}
