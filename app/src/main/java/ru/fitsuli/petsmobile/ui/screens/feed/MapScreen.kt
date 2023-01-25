package ru.fitsuli.petsmobile.ui.screens.feed

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import ru.fitsuli.petsmobile.data.dto.Gender
import ru.fitsuli.petsmobile.ui.components.GoogleMaps
import ru.fitsuli.petsmobile.ui.components.RoundedSurface
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold
import ru.fitsuli.petsmobile.ui.screens.PageType

/**
 * Created by Dmitry Danilyuk at 15.11.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onNavigateToAnimal: (id: String, pageType: PageType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapScreenViewModel = viewModel()
) {
    var selectedPageType by remember { mutableStateOf(PageType.ANIMAL_LOST) }
    val scope = rememberCoroutineScope()

    SimpleScaffold(
        modifier = modifier,
        useLargeTopAppBar = false,
        headerText = "Карта животных",
        actions = {
            IconButton(onClick = viewModel::fetchAll) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = "Refresh"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val isLost = selectedPageType == PageType.ANIMAL_LOST
                FilterChip(
                    selected = isLost,
                    onClick = { selectedPageType = PageType.ANIMAL_LOST },
                    label = { Text("Потерянные") },
                    modifier = Modifier.animateContentSize(),
                    leadingIcon = if (isLost) {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Localized Description",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                val isFound = selectedPageType == PageType.ANIMAL_FOUND
                FilterChip(
                    selected = isFound,
                    onClick = { selectedPageType = PageType.ANIMAL_FOUND },
                    label = { Text("Найденные") },
                    modifier = Modifier.animateContentSize(),
                    leadingIcon = if (isFound) {
                        {
                            Icon(
                                imageVector = Icons.Rounded.NearMe,
                                contentDescription = "Localized Description",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            RoundedSurface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Crossfade(targetState = selectedPageType) { pageType ->
                    GoogleMaps(
                        center = LatLng(56.826961, 60.603849),
                    ) {
                        viewModel.animals[pageType]?.forEach { animal ->
                            val latlng by remember {
                                derivedStateOf {
                                    val (long, lat) = animal.geoPosition.split(" ")
                                        .map { it.toDouble() }
                                    LatLng(lat, long)
                                }
                            }
                            val location =
                                animal.addressFull.takeIf { it.isNotBlank() } ?: animal.addressCity
                            Marker(
                                state = rememberMarkerState(position = latlng),
                                onInfoWindowClick = {
                                    scope.launch {
                                        onNavigateToAnimal(
                                            animal.id,
                                            if (animal.animalName != null) PageType.ANIMAL_LOST else PageType.ANIMAL_FOUND
                                        )
                                    }
                                },
                                title = when {
                                    animal.animalName == null -> "Нашли ${animal.animalType}; $location"
                                    animal.gender == Gender.MALE.value -> "Потерялся ${animal.animalName}; $location"
                                    else -> "Потерялась ${animal.animalName}; $location"
                                },
                            )
                        }
                    }
                }

            }
        }
    }
}