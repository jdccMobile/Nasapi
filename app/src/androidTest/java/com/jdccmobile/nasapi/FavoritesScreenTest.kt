package com.jdccmobile.nasapi

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.nasapi.ui.components.LOADING_INDICATOR_TAG
import com.jdccmobile.nasapi.ui.features.favorites.FavoritesScreen
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import kotlinx.collections.immutable.toImmutableList
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class FavoritesScreenTest {

    private val eventsUiMock = List(3){
        AstronomicEventUi(
            id = AstronomicEventId(it.toString()),
            title = "Title $it",
            description = "Description + $it",
            date = LocalDate.now(),
            imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
            isFavorite = false,
            hasImage = false,
        )
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `When loading state show progress`(): Unit = with(composeTestRule) {
        setContent {
            FavoritesScreen(
                favoriteEvents = emptyList<AstronomicEventUi>().toImmutableList(),
                isDataLoading = true,
                onFavoriteEventClicked = {},
                onBackNavigation = {},
            )
        }
        onNodeWithTag(LOADING_INDICATOR_TAG).assertIsDisplayed()
    }


    @Test
    fun `When there are no favorites show alert message`(): Unit = with(composeTestRule) {
        setContent {
            FavoritesScreen(
                favoriteEvents = emptyList<AstronomicEventUi>().toImmutableList(),
                isDataLoading = false,
                onFavoriteEventClicked = {},
                onBackNavigation = {},
            )
        }
        onNodeWithText("There are not favorites").assertExists()
    }

    @Test
    fun `When success state show favorite astronomic events`(): Unit = with(composeTestRule) {
        setContent {
            FavoritesScreen(
                favoriteEvents = eventsUiMock.toImmutableList(),
                isDataLoading = false,
                onFavoriteEventClicked = {},
                onBackNavigation = {},
            )
        }
        onNodeWithText("Title 1").assertExists()
    }

    @Test
    fun `When favorite astronomic event clicked listener is called`(): Unit = with(composeTestRule) {
        var clickedEventId = "-1"
        setContent {
            FavoritesScreen(
                favoriteEvents = eventsUiMock.toImmutableList(),
                isDataLoading = false,
                onFavoriteEventClicked = { clickedEventId = it},
                onBackNavigation = {},
            )
        }
        onNodeWithText("Title 1").performClick()

        assertEquals("1", clickedEventId)
    }

}