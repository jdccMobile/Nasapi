package com.jdccmobile.nasapi.ui.features.details

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jdccmobile.domain.model.AstronomicEventId
import com.jdccmobile.nasapi.R
import com.jdccmobile.nasapi.ui.components.Camera
import com.jdccmobile.nasapi.ui.components.CircularProgressBar
import com.jdccmobile.nasapi.ui.components.DetailsScaffold
import com.jdccmobile.nasapi.ui.components.IconAndMessageInfo
import com.jdccmobile.nasapi.ui.components.ImageWithErrorIcon
import com.jdccmobile.nasapi.ui.model.AstronomicEventPhotoUi
import com.jdccmobile.nasapi.ui.model.AstronomicEventUi
import com.jdccmobile.nasapi.ui.theme.Dimens
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import java.io.File
import java.time.LocalDate

@OptIn(KoinExperimentalAPI::class)
@Composable
fun DetailsDestination(
    astronomicEventId: String,
    onNavBack: () -> Unit,
) {
    val screenActions = DetailsScreenActions(
        onNavBack = onNavBack,
    )

    val viewModel: DetailsViewModel = koinViewModel(
        parameters = {
            parametersOf(astronomicEventId, screenActions)
        },
    )

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.onOpenCameraClicked()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.camera_permission_denied),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        },
    )

    DetailsScreen(viewModel = viewModel, permissionLauncher = permissionLauncher)
}

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailsContent(
        astronomicEvent = uiState.astronomicEvent,
        isLoading = uiState.isLoading,
        showCameraView = uiState.showCameraView,
        userPhotos = uiState.userPhotos,
        onFavoriteFabClicked = viewModel::onSwitchFavStatusClicked,
        onTakePhotoFabClicked = { requestCameraPermission(context, permissionLauncher, viewModel) },
        onSavePhotoTaken = viewModel::onSavePhotoTaken,
        onDeleteUserPhoto = viewModel::onDeletePhoto,
        onCloseCamera = viewModel::onCloseCamera,
        onBackNavigation = viewModel::onNavBack,
    )
}

@Suppress("MagicNumber", "LongParameterList")
@Composable
private fun DetailsContent(
    astronomicEvent: AstronomicEventUi?,
    isLoading: Boolean,
    showCameraView: Boolean,
    userPhotos: List<AstronomicEventPhotoUi>,
    onFavoriteFabClicked: () -> Unit,
    onTakePhotoFabClicked: () -> Unit,
    onSavePhotoTaken: (AstronomicEventPhotoUi, File, ByteArray) -> Unit,
    onDeleteUserPhoto: (AstronomicEventPhotoUi) -> Unit,
    onCloseCamera: () -> Unit,
    onBackNavigation: () -> Unit,
) {
    val listState = rememberLazyListState()
    val showBackFab by remember { derivedStateOf { listState.firstVisibleItemScrollOffset == 0 } }
    val favoriteFabIcon = getFavoriteFabIcon(astronomicEvent)

    DetailsScaffold(
        showBackFab = showBackFab,
        showAllFabs = !showCameraView,
        favoriteFabIcon = favoriteFabIcon,
        onFavoriteFabClicked = onFavoriteFabClicked,
        onTakePhotoFabClicked = onTakePhotoFabClicked,
        onBackNavigation = onBackNavigation,
    ) {
        if (isLoading) {
            CircularProgressBar()
        } else {
            if (showCameraView) {
                Camera(
                    eventId = astronomicEvent?.id ?: AstronomicEventId(""),
                    onSavePhotoTaken = onSavePhotoTaken,
                    onCloseCamera = onCloseCamera,
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy((-48).dp),
                    state = listState,
                ) {
                    item {
                        ImageWithErrorIcon(
                            imageUrl = astronomicEvent?.imageUrl,
                            modifier = Modifier.height(400.dp),
                        )
                    }
                    item { astronomicEvent?.let { EventDescription(astronomicEvent = it) } }
                    item {
                        MyPhotos(
                            userPhotos = userPhotos,
                            onDeleteUserPhoto = onDeleteUserPhoto,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventDescription(
    astronomicEvent: AstronomicEventUi,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            Text(
                text = astronomicEvent.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Text(
                text = astronomicEvent.date.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            Text(
                text = astronomicEvent.description,
                style = MaterialTheme.typography.bodySmall,
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyPhotos(
    userPhotos: List<AstronomicEventPhotoUi>,
    onDeleteUserPhoto: (AstronomicEventPhotoUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)) {
        MyPhotosTitle()
        if (userPhotos.isEmpty()) {
            IconAndMessageInfo(infoText = stringResource(R.string.there_are_no_photos))
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 16.dp),
            ) {
                items(userPhotos, key = { it.photoId.value }) { photo ->
                    MyPhotoCard(
                        photo = photo,
                        onDeleteUserPhoto = onDeleteUserPhoto,
                        modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                    )
                }
            }
        }
    }
}

@Composable
private fun MyPhotoCard(
    photo: AstronomicEventPhotoUi,
    onDeleteUserPhoto: (AstronomicEventPhotoUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    val photoPath = BitmapFactory.decodeFile(photo.filePath)

    Card(
        onClick = {
            // TODO open full image
        },
        modifier = modifier.height(200.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                bitmap = photoPath.asImageBitmap(),
                contentDescription = null,
            )
            IconButton(
                onClick = { onDeleteUserPhoto(photo) },
                modifier = Modifier
                    .size(Dimens.minTouchSize)
                    .align(Alignment.TopEnd),
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun MyPhotosTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.my_photos),
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(bottom = 16.dp),
    )
}

fun requestCameraPermission(
    context: Context,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    viewModel: DetailsViewModel,
) {
    val permission = android.Manifest.permission.CAMERA
    if (ContextCompat.checkSelfPermission(
            context,
            permission,
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        permissionLauncher.launch(permission)
    } else {
        viewModel.onOpenCameraClicked()
    }
}

private fun getFavoriteFabIcon(astronomicEvent: AstronomicEventUi?) =
    if (astronomicEvent?.isFavorite == true) {
        Icons.Outlined.Favorite
    } else {
        Icons.Outlined.FavoriteBorder
    }

@Preview
@Composable
private fun HomeScreenDestinationPreview() {
    NasapiTheme {
        DetailsContent(
            astronomicEvent = AstronomicEventUi(
                id = AstronomicEventId("1"),
                title = "Prueba",
                description = "Descripcion",
                date = LocalDate.now(),
                imageUrl = "https://apod.nasa.gov/apod/image/2408/2024MaUrM45.jpg",
                isFavorite = false,
                hasImage = false,
            ),
            isLoading = false,
            showCameraView = false,
            onFavoriteFabClicked = {},
            onTakePhotoFabClicked = {},
            userPhotos = emptyList(),
            onSavePhotoTaken = { _, _, _ -> },
            onDeleteUserPhoto = {},
            onCloseCamera = {},
            onBackNavigation = {},
        )
    }
}
