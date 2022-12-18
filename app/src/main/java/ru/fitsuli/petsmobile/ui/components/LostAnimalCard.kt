package ru.fitsuli.petsmobile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.fitsuli.petsmobile.data.dto.LostAnimalEntity

/**
 * Created by Dmitry Danilyuk at 19.12.2022
 */
@Composable
fun LostAnimalCard(
    onClick: () -> Unit,
    animal: LostAnimalEntity,
    modifier: Modifier = Modifier,
) {
    ClickableRoundedSurface(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = animal.animalName)
            Text(text = animal.animalType)
            Text(text = animal.description)
            Text(text = animal.lostArea)
            Text(text = animal.lostDate)
            Text(text = animal.fileNames.toString())

            animal.fileNames.forEach { fileName ->
                AsyncImage(
                    model = "http://10.0.2.2:7257/LostAnimalsImages/$fileName",
                    contentDescription = "Animal",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}