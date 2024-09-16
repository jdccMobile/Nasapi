package com.jdccmobile.nasapi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.theme.Dimens
import kotlinx.collections.immutable.ImmutableList

@Composable
fun InfiniteScrollLazyColumn(
    data: ImmutableList<AstronomicEventUi>,
    onItemClick: (String) -> Unit,
    onLoadMoreItems: () -> Unit,
    isMoreDataLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.appPadding),
    ) {
        itemsIndexed(data) { index, event ->
            if (index == data.lastIndex) {
                onLoadMoreItems()
            }
            CardItem(
                astronomicEventUi = event,
                onClick = onItemClick,
            )
        }
        if (isMoreDataLoading) {
            item { CircularProgressBar(modifier = Modifier.padding(vertical = 16.dp)) }
        }
    }
}
