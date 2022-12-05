package ru.fitsuli.petsmobile.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NearMe
import androidx.compose.material.icons.rounded.NightShelter
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import ru.fitsuli.petsmobile.R

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
enum class BottomBarTabs(
    val route: String,
    @StringRes val labelResId: Int,
    val icon: ImageVector
) {
    LOST(
        route = Destinations.LOST,
        labelResId = R.string.lost,
        icon = Icons.Rounded.Search
    ),

    FOUND(
        route = Destinations.FOUND,
        labelResId = R.string.found,
        icon = Icons.Rounded.NearMe
    ),

    SHELTER(
        route = Destinations.SHELTER,
        labelResId = R.string.shelter,
        icon = Icons.Rounded.NightShelter
    ),

    PROFILE(
        route = Destinations.PROFILE,
        labelResId = R.string.profile,
        icon = Icons.Rounded.Person
    )
}
