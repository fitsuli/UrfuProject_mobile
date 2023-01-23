package ru.fitsuli.petsmobile.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.fitsuli.petsmobile.R
import ru.fitsuli.petsmobile.ui.screens.AddAnimalScreen
import ru.fitsuli.petsmobile.ui.screens.InnerAnimalPage
import ru.fitsuli.petsmobile.ui.screens.ProfileScreen
import ru.fitsuli.petsmobile.ui.screens.SignInScreen
import ru.fitsuli.petsmobile.ui.screens.feed.FoundScreen
import ru.fitsuli.petsmobile.ui.screens.feed.LostScreen
import ru.fitsuli.petsmobile.ui.screens.feed.MapScreen

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(windowSizeClass: WindowSizeClass) {
    val isLandscape = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val navController = rememberNavController()
    val appState = remember(navController) { AppState(navController) }

    val onBackPressed: () -> Unit = { navController.popBackStack() }
    val onOpenAnimal: (animalId: String) -> Unit = {
        navController.navigate(Destinations.INNER_ANIMAL_PAGE + "?animalId=$it")
    }

    val onOpenAddAnimal: () -> Unit = {
        navController.navigate(Destinations.ADD_ANIMAL)
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = !isLandscape && appState.shouldShowBottomBar,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
            ) {
                NavigationBar(
                    tabs = appState.bottomBarTabs,
                    currentRoute = appState.currentRoute.orEmpty(),
                    onTabSelected = appState::navigateToBottomBarRoute,
                    onOpenAddAnimal = onOpenAddAnimal
                )
            }
        }
    ) { paddingValues ->
        Row {
            AnimatedVisibility(
                visible = isLandscape && appState.shouldShowBottomBar,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                NavigationRail(
                    tabs = appState.bottomBarTabs,
                    currentRoute = appState.currentRoute.orEmpty(),
                    onTabSelected = appState::navigateToBottomBarRoute,
                    onOpenAddAnimal = onOpenAddAnimal
                )
            }

            NavHost(
                appState.navController, startDestination = BottomBarTabs.LOST.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(BottomBarTabs.MAP.route) {
                    MapScreen(
                        onNavigateToPet = {
                            navController.navigate(Destinations.INNER_MAP)
                        }
                    )
                }
                composable(BottomBarTabs.LOST.route) {
                    LostScreen(
                        onOpenAnimal = onOpenAnimal
                    )
                }
                composable(BottomBarTabs.FOUND.route) {
                    FoundScreen(
                        onOpenAnimal = onOpenAnimal
                    )
                }
                composable(BottomBarTabs.PROFILE.route) {
                    ProfileScreen(
                        onNavigateToSignIn = { navController.navigate(Destinations.SIGN_IN) }
                    )
                }

                composable(Destinations.SIGN_IN) {
                    SignInScreen(onBackPressed = onBackPressed)
                }
                composable(Destinations.ADD_ANIMAL) {
                    AddAnimalScreen(
                        onBackPressed = onBackPressed
                    )
                }

                composable(Destinations.INNER_ANIMAL_PAGE + "?animalId={animalId}",
                    arguments = listOf(navArgument("animalId") { type = NavType.StringType }
                    )) {
                    it.arguments?.getString("animalId")?.let { animalId ->
                        InnerAnimalPage(animalId, isLandscape, onBackPressed)
                    }
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
    onOpenAddAnimal: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier,
        actions = {
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onOpenAddAnimal) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
            }
        }
    )
}

@Composable
private fun NavigationRail(
    tabs: Array<BottomBarTabs>,
    currentRoute: String,
    onOpenAddAnimal: () -> Unit,
    onTabSelected: (route: String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier
            .padding(16.dp),
        header = {
            FloatingActionButton(onClick = onOpenAddAnimal) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.create_post)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }) {
        tabs.forEach { tab ->
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = stringResource(id = tab.labelResId)
                    )
                },
                label = {
                    Text(text = stringResource(id = tab.labelResId))
                }, alwaysShowLabel = false,
                selected = currentRoute == tab.route,
                onClick = { onTabSelected(tab.route) },
                modifier = Modifier
                    .padding(top = 4.dp)
            )
        }
    }
}