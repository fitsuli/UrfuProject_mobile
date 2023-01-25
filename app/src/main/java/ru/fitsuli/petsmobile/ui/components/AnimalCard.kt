package ru.fitsuli.petsmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.fitsuli.petsmobile.data.dto.AnimalEntity
import ru.fitsuli.petsmobile.ui.screens.PageType
import ru.fitsuli.petsmobile.utils.Utils.formatDateDefault

/**
 * Created by Dmitry Danilyuk at 19.12.2022
 */
@Composable
fun AnimalCard(
    onClick: () -> Unit,
    cardType: PageType,
    animal: AnimalEntity,
    modifier: Modifier = Modifier,
) {
    val date by remember {
        derivedStateOf {
            formatDateDefault(
                animal.date,
                pattern = "dd MMMM yyyy"
            )
        }
    }
    RoundedSurface(
        onClick = if (animal.isClosed) {
            {}
        } else onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .clip(DefaultShape)
                    .aspectRatio(2f)
            ) {
                animal.fileNames.getOrNull(0)?.let { fileName ->
                    AsyncImage(
                        model = "http://localhost:7257/AnimalsImages/$fileName",
                        contentDescription = "Animal",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(DefaultShape)
                            .aspectRatio(2f)
                    )
                } ?: Text(text = "No image")

                if (animal.isClosed) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                            ), contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape,
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Done,
                                    contentDescription = "Done"
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = if (cardType == PageType.ANIMAL_LOST) "Питомец найден" else "Хозяин найден",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (cardType == PageType.ANIMAL_LOST) {
                    Text(
                        text = animal.animalName.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "•")
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = animal.animalType,
                    style = if (cardType == PageType.ANIMAL_LOST)
                        MaterialTheme.typography.bodyLarge
                    else
                        MaterialTheme.typography.titleMedium
                )
            }


            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = animal.addressCity.ifEmpty { animal.addressFull },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}