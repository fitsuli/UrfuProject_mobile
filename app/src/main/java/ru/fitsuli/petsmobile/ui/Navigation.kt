package ru.fitsuli.petsmobile.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.fitsuli.petsmobile.R
import ru.fitsuli.petsmobile.ui.screens.*
import ru.fitsuli.petsmobile.ui.screens.feed.FoundScreen
import ru.fitsuli.petsmobile.ui.screens.feed.LostScreen
import ru.fitsuli.petsmobile.ui.screens.feed.MapScreen

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    windowSizeClass: WindowSizeClass,
    globalViewModel: GlobalViewModel = viewModel()
) {
    val isLandscape = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val navController = rememberNavController()
    val appState = remember(navController) { AppState(navController) }

    val onBackPressed: () -> Unit = { navController.popBackStack() }
    val onOpenAnimal: (animalId: String, pageType: PageType) -> Unit = { animalId, pageType ->
        navController.navigate(Destinations.INNER_ANIMAL_PAGE + "?animalId=$animalId" + "&typePrefix=${pageType.urlPrefix}")
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
                    onOpenAddAnimal = onOpenAddAnimal,
                    isLoggedIn = globalViewModel.userInfo != null,
                    onOpenLogin = {
                        appState.navigateToBottomBarRoute(Destinations.PROFILE)
                    }
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
                        onNavigateToAnimal = { id, pageType ->
                            navController.navigate(
                                Destinations.INNER_ANIMAL_PAGE + "?animalId=$id" + "&typePrefix=${pageType.urlPrefix}"
                            )
                        }
                    )
                }
                composable(BottomBarTabs.LOST.route) {
                    LostScreen(
                        onOpenAnimal = {
                            onOpenAnimal(it, PageType.ANIMAL_LOST)
                        }
                    )
                }
                composable(BottomBarTabs.FOUND.route) {
                    FoundScreen(
                        onOpenAnimal = {
                            onOpenAnimal(it, PageType.ANIMAL_FOUND)
                        }
                    )
                }
                composable(Destinations.PROFILE) {
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

                composable(Destinations.INNER_ANIMAL_PAGE + "?animalId={animalId}" + "&typePrefix={typePrefix}",
                    arguments = listOf(
                        navArgument("animalId") { type = NavType.StringType },
                        navArgument("typePrefix") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    backStackEntry.arguments?.getString("animalId")?.let { animalId ->
                        backStackEntry.arguments?.getString("typePrefix")?.let { typePrefix ->
                            val pageType = PageType.values().find { it.urlPrefix == typePrefix }
                                ?: PageType.ANIMAL_LOST
                            InnerAnimalPage(animalId, pageType, onBackPressed)
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun NavigationBar(
    tabs: Array<BottomBarTabs>,
    currentRoute: String,
    isLoggedIn: Boolean,
    onOpenLogin: () -> Unit,
    onOpenAddAnimal: () -> Unit,
    onTabSelected: (route: String) -> Unit,
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
//                    alwaysShowLabel = false,
                    onClick = { onTabSelected(tab.route) }
                )
            }
        },
        floatingActionButton = {
            AnimatedContent(targetState = isLoggedIn) { isLoggedIn ->
                if (!isLoggedIn) {
                    FloatingActionButton(onClick = onOpenLogin) {
                        Icon(imageVector = Icons.Rounded.Person, contentDescription = "Login")
                    }
                } else {
                    FloatingActionButton(onClick = onOpenAddAnimal) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
                    }
                }
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