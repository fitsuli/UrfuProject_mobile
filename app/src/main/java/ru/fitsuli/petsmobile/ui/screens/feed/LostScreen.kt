package ru.fitsuli.petsmobile.ui.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.fitsuli.petsmobile.ui.components.LostAnimalCard
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LostScreen(
    onOpenAnimal: (String) -> Unit,
    onOpenAddAnimal: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LostScreenViewModel = viewModel()
) {
    SimpleScaffold(
        modifier = modifier.fillMaxSize(),
        headerText = stringResource(id = viewModel.tabInfo.labelResId),
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onOpenAddAnimal) {
                Icon(
                    imageVector = Icons.Rounded.Add, contentDescription = "Add animal",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
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