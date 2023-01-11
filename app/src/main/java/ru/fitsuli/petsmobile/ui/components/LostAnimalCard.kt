package ru.fitsuli.petsmobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.fitsuli.petsmobile.data.dto.LostAnimalEntity
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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

            animal.fileNames.getOrNull(0)?.let { fileName ->
                AsyncImage(
                    model = "http://10.0.2.2:7257/LostAnimalsImages/$fileName",
                    contentDescription = "Animal",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(DefaultShape)
                )
            } ?: Text(text = "No image")

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = animal.animalName,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = animal.animalType,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = animal.lostAddressFull)

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = ZonedDateTime.ofInstant(
                    Instant.parse(animal.lostDate),
                    ZoneId.systemDefault()
                ).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
            )
        }
    }
}