package com.jdccmobile.nasapi.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeDestination

@Serializable
object FavoritesDestination

@Serializable
data class DetailsDestination(val astronomicEventId: String)
