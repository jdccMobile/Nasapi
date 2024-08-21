package com.jdccmobile.nasapi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.jdccmobile.nasapi.ui.theme.NasapiTheme
import com.jdccmobile.nasapi.ui.theme.background
import com.jdccmobile.nasapi.ui.theme.cardContainer
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
        containerColor = background, // TODO añadir tema
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontFamily = montserratFontFamily,
                        color = Color.White,
                    )
                },
                scrollBehavior = scrollBehavior,
                actions = { actions?.let { it() } },
                colors = TopAppBarDefaults.topAppBarColors(
                    // TODO añadir tema
                    scrolledContainerColor = cardContainer,
                    containerColor = cardContainer,
                ),
//                TopAppBarDefaults.largeTopAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
//                    navigationIconContentColor = topAppBarElementColor,
//                    titleContentColor = topAppBarElementColor,
//                    actionIconContentColor= topAppBarElementColor,
//                )
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
    modifier: Modifier = Modifier,
    actions: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // TODO navegar atras
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = { actions?.let { it() } },
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
        TopBarWithNavigationScaffold("Favorites") {}
    }
}