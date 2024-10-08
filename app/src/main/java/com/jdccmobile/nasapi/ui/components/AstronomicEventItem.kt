package com.jdccmobile.nasapi.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import java.time.LocalDate

@Composable
fun AstronomicEventItem(
    astronomicEventUi: AstronomicEventUi,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onClick(astronomicEventUi.id.value) },
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
        ) {
            ImageWithErrorIcon(
                imageUrl = astronomicEventUi.imageUrl,
                modifier = Modifier.height(200.dp),
            )

            Text(
                text = astronomicEventUi.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .basicMarquee(),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
            ) {
                Text(
                    text = astronomicEventUi.date.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (astronomicEventUi.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CardItemPreview() {
    NasapiTheme {
        AstronomicEventItem(
            astronomicEventUi = AstronomicEventUi(
                id = AstronomicEventId("1"),
                title = "Prueba",
                description = "Descripcion",
                date = LocalDate.now(),
                imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
                isFavorite = false,
                hasImage = false,
            ),
            onClick = {},
        )
    }
}
