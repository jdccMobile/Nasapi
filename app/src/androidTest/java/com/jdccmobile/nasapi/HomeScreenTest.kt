package com.jdccmobile.nasapi

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.nasapi.ui.components.LOADING_INDICATOR_TAG
import com.jdccmobile.nasapi.ui.features.home.ErrorUi
import com.jdccmobile.nasapi.ui.features.home.HomeScreen
import com.jdccmobile.nasapi.ui.features.home.LoadingType
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import kotlinx.collections.immutable.toImmutableList
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class HomeScreenTest {

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
            HomeScreen(
                astronomicEvents = emptyList<AstronomicEventUi>().toImmutableList(),
                thereIsFavEvents = false,
                isInitialDataLoading = true,
                isMoreDataLoading = false,
                error = null,
                onLoadMoreItems = {},
                navigateToDetails = {},
                navigateToFavorites = {},
            )
        }
        onNodeWithTag(LOADING_INDICATOR_TAG).assertIsDisplayed()
    }


    @Test
    fun `When error state show error message`(): Unit = with(composeTestRule) {
        setContent {
            HomeScreen(
                astronomicEvents = emptyList<AstronomicEventUi>().toImmutableList(),
                thereIsFavEvents = false,
                isInitialDataLoading = false,
                isMoreDataLoading = false,
                error = ErrorUi("Network connection", LoadingType.InitialLoading),
                onLoadMoreItems = {},
                navigateToDetails = {},
                navigateToFavorites = {},
            )
        }
        onNodeWithText("Network connection").assertExists()
    }

    @Test
    fun `When success state show astronomic events`(): Unit = with(composeTestRule) {
        setContent {
            HomeScreen(
                astronomicEvents = eventsUiMock.toImmutableList(),
                thereIsFavEvents = false,
                isInitialDataLoading = false,
                isMoreDataLoading = false,
                error = null,
                onLoadMoreItems = {},
                navigateToDetails = {},
                navigateToFavorites = {},
            )
        }
        onNodeWithText("Title 1").assertExists()
    }

    @Test
    fun `When astronomic event clicked listener is called`(): Unit = with(composeTestRule) {
        var clickedEventId = "-1"
        setContent {
            HomeScreen(
                astronomicEvents = eventsUiMock.toImmutableList(),
                thereIsFavEvents = false,
                isInitialDataLoading = false,
                isMoreDataLoading = false,
                error = null,
                onLoadMoreItems = {},
                navigateToDetails = { clickedEventId = it},
                navigateToFavorites = {},
            )
        }
        onNodeWithText("Title 1").performClick()

        assertEquals("1", clickedEventId)
    }

}