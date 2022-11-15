package ru.fitsuli.petsmobile.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.fitsuli.petsmobile.ui.screens.FoundScreen
import ru.fitsuli.petsmobile.ui.screens.InnerShelterScreen
import ru.fitsuli.petsmobile.ui.screens.LostScreen
import ru.fitsuli.petsmobile.ui.screens.ShelterScreen

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val appState = remember(navController) { AppState(navController) }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = appState.shouldShowBottomBar,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
            ) {
                NavigationBar(
                    tabs = appState.bottomBarTabs,
                    currentRoute = appState.currentRoute.orEmpty(),
                    onTabSelected = appState::navigateToBottomBarRoute
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            appState.navController, startDestination = BottomBarTabs.SHELTER.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomBarTabs.SHELTER.route) {
                ShelterScreen(
                    onNavigateToPet = {
                        navController.navigate(Destinations.INNER_SHELTER)
                    }
                )
            }
            composable(BottomBarTabs.LOST.route) {
                LostScreen()
            }
            composable(BottomBarTabs.FOUND.route) {
                FoundScreen()
            }
            composable(Destinations.INNER_SHELTER) {
                InnerShelterScreen()
            }
        }
    }

}

@Composable
private fun NavigationBar(
    tabs: Array<BottomBarTabs>,
    currentRoute: String,
    onTabSelected: (route: String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        tabs.forEach { tab ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = stringResource(id = tab.labelResId)
                    )
                },
                label = {
                    Text(text = stringResource(id = tab.labelResId))
                },
                selected = currentRoute == tab.route,
                onClick = { onTabSelected(tab.route) }
            )
        }
    }
}