package com.jdccmobile.nasapi

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.nasapi.ui.components.CAMERA_FAB_TAG
import com.jdccmobile.nasapi.ui.components.FAVORITE_FAB_TAG
import com.jdccmobile.nasapi.ui.components.LOADING_INDICATOR_TAG
import com.jdccmobile.nasapi.ui.features.details.DetailsScreen
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class DetailsScreenTest {

    private val eventUiMock = AstronomicEventUi(
        id = AstronomicEventId("1"),
        title = "Title 1",
        description = "Description 1",
        date = LocalDate.now(),
        imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
        isFavorite = false,
        hasImage = false,
    )


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `When loading state show progress`(): Unit = with(composeTestRule) {
        setContent {
            DetailsScreen(
                astronomicEvent = null,
                isLoading = true,
                showCameraView = false,
                userPhotos = emptyList(),
                onFavoriteFabClicked = {},
                onTakePhotoFabClicked = {},
                onSavePhotoTaken = { _, _, _ -> },
                onDeleteUserPhoto = {},
                onCloseCamera = {},
                onBackNavigation = {},
            )
        }
        onNodeWithTag(LOADING_INDICATOR_TAG).assertIsDisplayed()
    }


    @Test
    fun `When success state show astronomic event details`(): Unit = with(composeTestRule) {
        setContent {
            DetailsScreen(
                astronomicEvent = eventUiMock,
                isLoading = false,
                showCameraView = false,
                userPhotos = emptyList(),
                onFavoriteFabClicked = {},
                onTakePhotoFabClicked = {},
                onSavePhotoTaken = { _, _, _ -> },
                onDeleteUserPhoto = {},
                onCloseCamera = {},
                onBackNavigation = {},
            )
        }
        onNodeWithText("Title 1").assertExists()
    }

    @Test
    fun `When open camera clicked camera is open`(): Unit = with(composeTestRule) {
        var showCameraView = false
        setContent {
            DetailsScreen(
                astronomicEvent = eventUiMock,
                isLoading = false,
                showCameraView = showCameraView,
                userPhotos = emptyList(),
                onFavoriteFabClicked = {},
                onTakePhotoFabClicked = { showCameraView = true},
                onSavePhotoTaken = { _, _, _ -> },
                onDeleteUserPhoto = {},
                onCloseCamera = {},
                onBackNavigation = {},
            )
        }
        onNodeWithTag(CAMERA_FAB_TAG).performClick()

        assertEquals(true, showCameraView)
    }

    @Test
    fun `When favorite clicked switch camera state`(): Unit = with(composeTestRule) {
        var astronomicEvent = eventUiMock
        setContent {
            DetailsScreen(
                astronomicEvent = eventUiMock,
                isLoading = false,
                showCameraView = false,
                userPhotos = emptyList(),
                onFavoriteFabClicked = { astronomicEvent = astronomicEvent.copy(isFavorite = !astronomicEvent.isFavorite)},
                onTakePhotoFabClicked = { },
                onSavePhotoTaken = { _, _, _ -> },
                onDeleteUserPhoto = {},
                onCloseCamera = {},
                onBackNavigation = {},
            )
        }
        onNodeWithTag(FAVORITE_FAB_TAG).performClick()

        assertEquals(true, astronomicEvent.isFavorite)
    }

}