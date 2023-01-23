package ru.fitsuli.petsmobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import ru.fitsuli.petsmobile.R
import ru.fitsuli.petsmobile.data.dto.AnimalEntity
import ru.fitsuli.petsmobile.data.dto.Contacts
import ru.fitsuli.petsmobile.data.dto.Gender
import ru.fitsuli.petsmobile.ui.components.GoogleMaps
import ru.fitsuli.petsmobile.ui.components.RoundedSurface
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold
import ru.fitsuli.petsmobile.utils.Utils

/**
 * Created by Dmitry Danilyuk at 19.12.2022
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun InnerAnimalPage(
    animalId: String,
    isLandscape: Boolean,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InnerAnimalPageViewModel = viewModel()
) {
    LaunchedEffect(animalId, viewModel) {
        viewModel.loadAnimal(animalId)
    }

    val headerText = viewModel.animal?.let {
        if (it.gender == Gender.MALE.value) "Потерялся ${it.animalName}" else "Потерялась ${it.animalName}"
    } ?: "Потерялся"

    SimpleScaffold(
        headerText = headerText,
        onBackPressed = onBackPressed,
        modifier = modifier
    ) { paddingValues ->
        AnimatedVisibility(
            visible = viewModel.animal != null,
            enter = fadeIn() + slideInVertically(
                initialOffsetY = { -it / 4 }
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                    //.height(intrinsicSize = IntrinsicSize.Max)
                ) {
                    HorizontalPager(
                        count = viewModel.animal!!.fileNames.size, key = { it },
                        itemSpacing = 12.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .run { if (isLandscape) this.aspectRatio(2f) else this.fillMaxHeight() }
                            .clip(RoundedCornerShape(8.dp))
                    ) { page ->
                        viewModel.animal?.fileNames?.getOrNull(page)?.let { fileName ->
                            AsyncImage(
                                model = "http://localhost:7257/AnimalsImages/$fileName",
                                contentDescription = "Animal",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }

                    if (isLandscape) {
                        DescriptionCard(
                            animal = viewModel.animal!!,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }

                if (!isLandscape) {
                    Spacer(modifier = Modifier.height(16.dp))
                    DescriptionCard(
                        animal = viewModel.animal!!
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                ContactsCard(
                    contacts = viewModel.animal!!.contacts,
                )

                Spacer(modifier = Modifier.height(16.dp))
                LocationCard(
                    animal = viewModel.animal!!,
                )
            }
        }
    }
}

@Composable
fun DescriptionCard(
    animal: AnimalEntity,
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
                style = MaterialTheme.typography.titleMedium
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
                style = MaterialTheme.typography.titleMedium
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
    animal: AnimalEntity,
    modifier: Modifier = Modifier
) {
    val formattedDate by remember {
        derivedStateOf { Utils.formatDateDefault(animal.date) }
    }
    RoundedSurface(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Место и время пропажи",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Город: ${animal.addressCity}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Место: ${animal.addressFull}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Дата: $formattedDate",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))
            RoundedSurface {
                val latlng by remember {
                    derivedStateOf {
                        val (long, lat) = animal.geoPosition.split(" ").map { it.toDouble() }
                        LatLng(lat, long)
                    }
                }
                GoogleMaps(
                    center = latlng,
                    modifier = Modifier
                        .aspectRatio(2f),
                    content = {
                        Marker(
                            state = MarkerState(position = latlng),
                            title = "Место пропажи"
                        )
                    }
                )
            }
        }
    }
}