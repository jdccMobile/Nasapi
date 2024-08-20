package com.jdccmobile.nasapi.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import com.jdccmobile.nasapi.ui.theme.cardAccent
import com.jdccmobile.nasapi.ui.theme.cardContainer
import com.jdccmobile.nasapi.ui.theme.proportionalFontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardItem(
    title: String,
    date: String,
    imageUrl: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(imageUrl) }, // TODO utilizar carddefaults colors
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(color = cardContainer),
            horizontalAlignment = Alignment.Start
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    fontFamily = proportionalFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = cardAccent,
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .basicMarquee(),
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontFamily = proportionalFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                ),
                modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp),
            )
        }
    }
}


@Preview
@Composable
private fun CardItemPreview() {
    NasapiTheme {
        CardItem(
            title = "Titulo",
            date = "2024/08/11",
            imageUrl = "",
            onClick = {}
        )
    }
}
