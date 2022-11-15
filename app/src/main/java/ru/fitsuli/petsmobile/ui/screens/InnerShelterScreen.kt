package ru.fitsuli.petsmobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InnerShelterScreen(
    modifier: Modifier = Modifier,
    viewModel: ShelterScreenViewModel = viewModel()
) {
    SimpleScaffold(
        modifier = modifier,
        headerText = stringResource(id = viewModel.tabInfo.labelResId)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(text = "Inner shelter")
        }
    }
}