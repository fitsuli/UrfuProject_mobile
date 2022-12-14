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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.fitsuli.petsmobile.ui.screens.AddAnimalScreen
import ru.fitsuli.petsmobile.ui.screens.InnerAnimalPage
import ru.fitsuli.petsmobile.ui.screens.InnerShelterScreen
import ru.fitsuli.petsmobile.ui.screens.ProfileScreen
import ru.fitsuli.petsmobile.ui.screens.SignInScreen
import ru.fitsuli.petsmobile.ui.screens.feed.FoundScreen
import ru.fitsuli.petsmobile.ui.screens.feed.LostScreen
import ru.fitsuli.petsmobile.ui.screens.feed.ShelterScreen

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val appState = remember(navController) { AppState(navController) }

    val onNavigateToAddAnimalScreen: (animalId: String) -> Unit = {
        navController.navigate(Destinations.INNER_ANIMAL_PAGE + "?animalId=$it")
    }

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
            appState.navController, startDestination = BottomBarTabs.LOST.route,
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
                LostScreen(
                    onOpenAddAnimal = { navController.navigate(Destinations.ADD_LOST_ANIMAL) },
                    onOpenAnimal = onNavigateToAddAnimalScreen
                )
            }
            composable(BottomBarTabs.FOUND.route) {
                FoundScreen()
            }
            composable(BottomBarTabs.PROFILE.route) {
                ProfileScreen(
                    onNavigateToSignIn = { navController.navigate(Destinations.SIGN_IN) }
                )
            }

            composable(Destinations.SIGN_IN) {
                SignInScreen()
            }
            composable(Destinations.ADD_LOST_ANIMAL) {
                AddAnimalScreen()
            }

            composable(Destinations.INNER_SHELTER) {
                InnerShelterScreen()
            }
            composable(Destinations.INNER_ANIMAL_PAGE + "?animalId={animalId}",
                arguments = listOf(navArgument("animalId") { type = NavType.StringType }
                )) {
                it.arguments?.getString("animalId")?.let { animalId ->
                    InnerAnimalPage(animalId)
                }
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