package com.jdccmobile.nasapi.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jdccmobile.nasapi.R
import com.jdccmobile.nasapi.ui.theme.Dimens
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import com.jdccmobile.nasapi.ui.theme.lightBlue
import com.jdccmobile.nasapi.ui.theme.montserratFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScaffold(
    title: String,
    modifier: Modifier = Modifier,
    actions: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontFamily = montserratFontFamily,
                    )
                },
                scrollBehavior = scrollBehavior,
                actions = { actions?.let { it() } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithNavigationScaffold(
    title: String,
    onBackNavigation: () -> Unit,
    modifier: Modifier = Modifier,
    actions: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
                    rememberTopAppBarState(),
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { onBackNavigation() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = { actions?.let { it() } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            content()
        }
    }
}

@Composable
fun DetailsScaffold(
    showBackFab: Boolean,
    showAllFabs: Boolean,
    favoriteFabIcon: ImageVector,
    onFavoriteFabClicked: () -> Unit,
    onBackNavigation: () -> Unit,
    onTakePhotoFabClicked: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = {
            if (showAllFabs) {
                Column {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        onClick = { onTakePhotoFabClicked() },
                        modifier = Modifier.testTag(CAMERA_FAB_TAG),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_a_photo),
                            contentDescription = null,
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                        onClick = { onFavoriteFabClicked() },
                                modifier = Modifier.testTag(FAVORITE_FAB_TAG),
                    ) {
                        Icon(imageVector = favoriteFabIcon, contentDescription = null)
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            content()
            if (showAllFabs) {
                AnimatedVisibility(visible = showBackFab, enter = fadeIn(), exit = fadeOut()) {
                    FloatingActionButton(
                        onClick = { onBackNavigation() },
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(Dimens.appPadding),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = { onClick() },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = lightBlue,
        )
    }
}

const val CAMERA_FAB_TAG = "cameraFabTag"
const val FAVORITE_FAB_TAG = "favoriteFabTag"

@Preview
@Composable
private fun TopBarScaffoldPreview() {
    NasapiTheme {
        TopBarScaffold("Nasapi") {}
    }
}

@Preview
@Composable
private fun TopBarWithNavigationScaffoldPreview() {
    NasapiTheme {
        TopBarWithNavigationScaffold("Favorites", onBackNavigation = {}) {}
    }
}

