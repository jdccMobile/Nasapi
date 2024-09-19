package com.jdccmobile.nasapi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.jdccmobile.nasapi.ui.features.details.DetailsDestination
import com.jdccmobile.nasapi.ui.features.favorites.FavoritesScreen
import com.jdccmobile.nasapi.ui.features.home.HomeDestination

@Composable
fun NavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeDestination,
    ) {
        composable<HomeDestination> {
            HomeDestination(
                navigateToFavorites = { navController.navigate(FavoritesDestination) },
                navigateToDetails = { astronomicEventId ->
                    navController.navigate(DetailsDestination(astronomicEventId))
                },
            )
        }
        composable<FavoritesDestination> {
            FavoritesScreen(
                onNavBack = { navController.popBackStack() },
            )
        }
        composable<DetailsDestination> { backStackEntry ->
            val detail = backStackEntry.toRoute<DetailsDestination>()
            DetailsDestination(
                astronomicEventId = detail.astronomicEventId,
                onNavBack = { navController.popBackStack() },
            )
        }
    }
}
