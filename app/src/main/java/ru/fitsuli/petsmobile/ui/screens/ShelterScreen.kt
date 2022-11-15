package ru.fitsuli.petsmobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.fitsuli.petsmobile.ui.components.PetItem
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold

/**
 * Created by Dmitry Danilyuk at 15.11.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelterScreen(
    onNavigateToPet: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShelterScreenViewModel = viewModel()
) {
    SimpleScaffold(
        modifier = modifier,
        headerText = stringResource(id = viewModel.tabInfo.labelResId)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(10) {
                PetItem(
                    title = "Title $it",
                    subtitle = "Subtitle",
                    onClick = { onNavigateToPet() },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}