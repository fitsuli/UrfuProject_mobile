package ru.fitsuli.petsmobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.fitsuli.petsmobile.R
import ru.fitsuli.petsmobile.data.dto.Contacts
import ru.fitsuli.petsmobile.data.dto.Gender
import ru.fitsuli.petsmobile.data.dto.LostAnimalEntity
import ru.fitsuli.petsmobile.ui.components.RoundedSurface
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold
import ru.fitsuli.petsmobile.ui.components.YandexMapKit
import ru.fitsuli.petsmobile.utils.Utils

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

            AnimatedVisibility(visible = viewModel.animal != null) {
                DescriptionCard(
                    animal = viewModel.animal!!,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                )
            }

            AnimatedVisibility(visible = viewModel.animal != null) {
                ContactsCard(
                    contacts = viewModel.animal!!.contacts,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                )
            }

            AnimatedVisibility(visible = viewModel.animal != null) {
                LocationCard(
                    animal = viewModel.animal!!,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                )
            }
            Text(text = "${viewModel.animal}")

        }
    }
}

@Composable
fun DescriptionCard(
    animal: LostAnimalEntity,
    modifier: Modifier = Modifier,
) {
    RoundedSurface(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Описание",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = animal.description)

            Spacer(modifier = Modifier.height(8.dp))
            RoundedSurface(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Животное",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = animal.animalType,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Пол",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = Gender.values().find { it.ordinal == animal.gender }?.text
                                ?: "Неизвестно",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Возраст",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = pluralStringResource(
                                id = R.plurals.plural_years, count = animal.age,
                                formatArgs = arrayOf(animal.age)
                            ),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun ContactsCard(
    contacts: Contacts,
    modifier: Modifier = Modifier,
) {
    RoundedSurface(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Контакты",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = contacts.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))
            ContactBlock(
                isVisible = contacts.phone.isNotEmpty(),
                icon = { Icon(imageVector = Icons.Rounded.Phone, contentDescription = "Phone") },
                text = contacts.phone
            )

            Spacer(modifier = Modifier.height(8.dp))
            ContactBlock(
                isVisible = contacts.email.isNotEmpty(),
                icon = { Icon(imageVector = Icons.Rounded.Email, contentDescription = "Email") },
                text = contacts.email
            )

            Spacer(modifier = Modifier.height(8.dp))
            ContactBlock(
                isVisible = contacts.telegram.isNotEmpty(),
                icon = { Icon(imageVector = Icons.Rounded.Chat, contentDescription = "Telegram") },
                text = contacts.telegram
            )

            Spacer(modifier = Modifier.height(8.dp))
            ContactBlock(
                isVisible = contacts.vk.isNotEmpty(),
                icon = { Icon(imageVector = Icons.Rounded.Chat, contentDescription = "VK") },
                text = contacts.vk
            )
        }
    }
}

@Composable
fun ContactBlock(
    isVisible: Boolean,
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        RoundedSurface(modifier) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                icon()
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun LocationCard(
    animal: LostAnimalEntity,
    modifier: Modifier = Modifier
) {
    val formattedDate by remember {
        derivedStateOf { Utils.formatDateDefault(animal.lostDate) }
    }
    RoundedSurface(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Место и время пропажи",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Город: ${animal.lostAddressCity}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Место: ${animal.lostAddressFull}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Дата: $formattedDate",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))
            RoundedSurface {
                YandexMapKit(
                    latitude = 55.75,
                    longitude = 37.57,
                    modifier = Modifier
                        .aspectRatio(2f),
//                    lat = animal.lostAddressLat,
//                    lon = animal.lostAddressLon
                )
            }
        }
    }
}