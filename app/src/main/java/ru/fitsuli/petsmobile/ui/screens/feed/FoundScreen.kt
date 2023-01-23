package ru.fitsuli.petsmobile.ui.screens.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.fitsuli.petsmobile.R
import ru.fitsuli.petsmobile.ui.components.LostAnimalCard
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoundScreen(
    modifier: Modifier = Modifier,
    onOpenAnimal: (String) -> Unit,
    viewModel: FoundScreenViewModel = viewModel()
) {
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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(400.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(viewModel.animalList, key = { it.id }) {
                LostAnimalCard(
                    animal = it,
                    onClick = { onOpenAnimal(it.id) }
                )
            }
        }
    }

}