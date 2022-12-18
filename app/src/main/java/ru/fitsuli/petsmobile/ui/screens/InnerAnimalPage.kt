package ru.fitsuli.petsmobile.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Created by Dmitry Danilyuk at 19.12.2022
 */
@Composable
fun InnerAnimalPage(
    animalId: String,
    modifier: Modifier = Modifier,
    viewModel: InnerAnimalPageViewModel = viewModel()
) {
    LaunchedEffect(animalId, viewModel) {
        viewModel.loadAnimal(animalId)
    }


}