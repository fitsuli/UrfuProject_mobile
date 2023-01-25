package ru.fitsuli.petsmobile.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.NearMe
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

    MAP(
        route = Destinations.MAP,
        labelResId = R.string.map,
        icon = Icons.Rounded.Map
    )
}
