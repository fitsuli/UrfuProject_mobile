package ru.fitsuli.petsmobile.ui.screens.feed

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.fitsuli.petsmobile.R
import ru.fitsuli.petsmobile.ui.components.AnimalCard
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold
import ru.fitsuli.petsmobile.ui.screens.AnimalType
import ru.fitsuli.petsmobile.ui.screens.PageType

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun FoundScreen(
    modifier: Modifier = Modifier,
    onOpenAnimal: (String) -> Unit,
    viewModel: FoundScreenViewModel = viewModel()
) {
    val state = rememberLazyListState()
    val items by viewModel.sorted.collectAsState()

    LaunchedEffect(items.firstOrNull()) {
        if (items.isNotEmpty()) {
            state.animateScrollToItem(0)
        }
    }
    SimpleScaffold(
        modifier = modifier.fillMaxSize(),
        headerText = stringResource(id = R.string.found_screen_header),
        actions = {
            IconButton(onClick = viewModel::getFoundAnimals) {
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
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            var filterAnimalType by remember { mutableStateOf(AnimalType.NONE) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                val sortByDescendingDate by viewModel.sortByDescendingDate.collectAsState()

                ElevatedAssistChip(
                    onClick = viewModel::toggleSortByDescendingDate,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Sort,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    },
                    label = {
                        AnimatedContent(targetState = sortByDescendingDate) {
                            Text(text = if (it) "Дата по убыванию" else "Дата по возрастанию")
                        }
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                val types = remember { AnimalType.values() }
                types.forEach {
                    FilterChip(
                        selected = filterAnimalType == it,
                        onClick = {
                            filterAnimalType = it
                            viewModel.setFilterAnimalType(it.localized)
                        },
                        label = { Text(it.localizedMany) },
                        modifier = Modifier.animateContentSize(),
                        leadingIcon = if (filterAnimalType == it) {
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
                }
            }

            LazyColumn(
                state = state,
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items, key = { it.id }) {
                    AnimalCard(
                        animal = it,
                        cardType = PageType.ANIMAL_FOUND,
                        onClick = { onOpenAnimal(it.id) },
                        modifier = Modifier
                            .animateItemPlacement()
                    )
                }
            }
        }
    }
}