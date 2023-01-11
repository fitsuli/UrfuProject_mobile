package ru.fitsuli.petsmobile.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.fitsuli.petsmobile.data.dto.Gender
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold

/**
 * Created by Dmitry Danilyuk at 19.12.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InnerAnimalPage(
    animalId: String,
    modifier: Modifier = Modifier,
    viewModel: InnerAnimalPageViewModel = viewModel()
) {
    LaunchedEffect(animalId, viewModel) {
        viewModel.loadAnimal(animalId)
    }

    val headerText = viewModel.animal?.let {
        if (it.gender == Gender.MALE.value) "Потерялся ${it.animalName}" else "Потерялась ${it.animalName}"
    } ?: "Потерялся"

    SimpleScaffold(headerText = headerText, modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier
                    .aspectRatio(1f)
                    .horizontalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                viewModel.animal?.fileNames?.forEach { fileName ->
                    AsyncImage(
                        model = "http://10.0.2.2:7257/LostAnimalsImages/$fileName",
                        contentDescription = "Animal",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                    )
                } ?: Text(text = "No images")
            }
            Text(text = "${viewModel.animal}")
        }
    }
}