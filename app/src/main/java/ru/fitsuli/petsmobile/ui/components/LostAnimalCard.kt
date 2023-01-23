package ru.fitsuli.petsmobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.fitsuli.petsmobile.data.dto.AnimalEntity
import ru.fitsuli.petsmobile.utils.Utils.formatDateDefault

/**
 * Created by Dmitry Danilyuk at 19.12.2022
 */
@Composable
fun LostAnimalCard(
    onClick: () -> Unit,
    animal: AnimalEntity,
    modifier: Modifier = Modifier,
) {
    RoundedSurface(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            animal.fileNames.getOrNull(0)?.let { fileName ->
                AsyncImage(
                    model = "http://localhost:7257/AnimalsImages/$fileName",
                    contentDescription = "Animal",
                    modifier = Modifier
                        .clip(DefaultShape)
                )
            } ?: Text(text = "No image")

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = animal.animalName.toString(),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = animal.animalType,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = animal.addressFull.toString())

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatDateDefault(animal.date),
            )
        }
    }
}