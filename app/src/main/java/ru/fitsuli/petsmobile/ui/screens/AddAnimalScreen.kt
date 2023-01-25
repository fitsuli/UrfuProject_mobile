package ru.fitsuli.petsmobile.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import ru.fitsuli.petsmobile.R
import ru.fitsuli.petsmobile.data.dto.Gender
import ru.fitsuli.petsmobile.ui.components.GoogleMaps
import ru.fitsuli.petsmobile.ui.components.RoundedSurface
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold
import ru.fitsuli.petsmobile.ui.components.ToggleButtonAnim

/**
 * Created by Dmitry Danilyuk at 14.12.2022
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AddAnimalScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    viewModel: AddAnimalScreenViewModel = viewModel()
) {
    var currentPage by remember { mutableStateOf(PageType.ANIMAL_LOST) }

    SimpleScaffold(
        headerText = stringResource(id = R.string.create_post),
        onBackPressed = onBackPressed,
        floatingActionButton = {
            AnimatedVisibility(visible = viewModel.isSendButtonVisible) {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.sendAnimal(
                            pageType = currentPage
                        )
                    },
                    icon = {
                        Icon(imageVector = Icons.Rounded.Done, contentDescription = "Done")
                    }, text = {
                        Text(text = "Отправить")
                    }
                )
            }
        }, modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ToggleButtonAnim(
                    onClick = { currentPage = PageType.ANIMAL_LOST },
                    checked = currentPage == PageType.ANIMAL_LOST,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Потерял питомца")
                }
                ToggleButtonAnim(
                    onClick = { currentPage = PageType.ANIMAL_FOUND },
                    checked = currentPage == PageType.ANIMAL_FOUND,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Нашел питомца")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(targetState = currentPage) { page ->
                Column {
                    RoundedSurface {
                        Column(
                            modifier = Modifier.padding(24.dp),
                        ) {

                            Text(text = "Выберите тип питомца")
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.horizontalScroll(rememberScrollState())
                            ) {
                                RadioButtonText(
                                    text = AnimalType.DOG.localized,
                                    selected = viewModel.createEntity.animalType == AnimalType.DOG.localized,
                                    onClick = { viewModel.setAnimalType(AnimalType.DOG.localized) },
                                )

                                Spacer(modifier = Modifier.width(16.dp))
                                RadioButtonText(
                                    text = AnimalType.CAT.localized,
                                    selected = viewModel.createEntity.animalType == AnimalType.CAT.localized,
                                    onClick = { viewModel.setAnimalType(AnimalType.CAT.localized) },
                                )

                                val isOtherSelected =
                                    viewModel.createEntity.animalType != AnimalType.DOG.localized
                                            && viewModel.createEntity.animalType != AnimalType.CAT.localized

                                Spacer(modifier = Modifier.width(16.dp))
                                AnimatedContent(targetState = isOtherSelected) { isSelected ->
                                    if (isSelected) {
                                        val text by remember { mutableStateOf(AnimalType.OTHER.localized) }

                                        OutlinedTextField(
                                            value = text,
                                            onValueChange = viewModel::setAnimalType,
                                            singleLine = true,
                                            shape = RoundedCornerShape(25),
                                        )
                                    } else {
                                        RadioButtonText(
                                            text = AnimalType.OTHER.localized,
                                            selected = isOtherSelected,
                                            onClick = { viewModel.setAnimalType(AnimalType.OTHER.localized) },
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(text = "Выберите пол и возраст питомца")
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                RadioButtonText(
                                    text = Gender.MALE.text,
                                    selected = viewModel.createEntity.gender == Gender.MALE.value,
                                    onClick = { viewModel.setGender(Gender.MALE) }
                                )

                                Spacer(modifier = Modifier.width(16.dp))
                                RadioButtonText(
                                    text = Gender.FEMALE.text,
                                    selected = viewModel.createEntity.gender == Gender.FEMALE.value,
                                    onClick = { viewModel.setGender(Gender.FEMALE) }
                                )

                                if (page == PageType.ANIMAL_FOUND) {
                                    Spacer(modifier = Modifier.width(16.dp))
                                    RadioButtonText(
                                        text = Gender.UNKNOWN.text,
                                        selected = viewModel.createEntity.gender == Gender.UNKNOWN.value,
                                        onClick = { viewModel.setGender(Gender.UNKNOWN) }
                                    )
                                }

                                Spacer(modifier = Modifier.width(24.dp))
                                OutlinedTextField(
                                    value = "${viewModel.createEntity.age}",
                                    onValueChange = { s ->
                                        s.toIntOrNull()?.let(viewModel::setAge)
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(25),
                                    trailingIcon = {
                                        Text(
                                            text = "лет",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }, modifier = Modifier
                                        .widthIn(min = 100.dp, max = 200.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    RoundedSurface {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {

                            Text(
                                text = "Контакты",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = viewModel.createEntity.contacts.name,
                                onValueChange = viewModel::setContactName,
                                singleLine = true,
                                shape = RoundedCornerShape(25),
                                label = { Text(text = "Имя") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = "Имя"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = viewModel.createEntity.contacts.phone,
                                onValueChange = viewModel::setContactPhone,
                                singleLine = true,
                                shape = RoundedCornerShape(25),
                                label = { Text(text = "Телефон") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Phone,
                                        contentDescription = "Телефон"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = viewModel.createEntity.contacts.email ?: "",
                                onValueChange = viewModel::setContactEmail,
                                singleLine = true,
                                shape = RoundedCornerShape(25),
                                label = { Text(text = "Email") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Email,
                                        contentDescription = "Email"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = viewModel.createEntity.contacts.telegram ?: "",
                                onValueChange = viewModel::setContactTelegram,
                                singleLine = true,
                                shape = RoundedCornerShape(25),
                                label = { Text(text = "Telegram") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Chat,
                                        contentDescription = "Telegram"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = viewModel.createEntity.contacts.vk ?: "",
                                onValueChange = viewModel::setContactVk,
                                singleLine = true,
                                shape = RoundedCornerShape(25),
                                label = { Text(text = "VK") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Tag,
                                        contentDescription = "VK"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    RoundedSurface {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Дополнительная информация",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = viewModel.createEntity.animalName ?: "",
                                onValueChange = viewModel::setAnimalName,
                                singleLine = true,
                                shape = RoundedCornerShape(25),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Pets,
                                        contentDescription = "Кличка питомца"
                                    )
                                },
                                label = { Text(text = "Кличка питомца") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = viewModel.createEntity.description,
                                onValueChange = viewModel::setDescription,
                                singleLine = false,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Info,
                                        contentDescription = "Дополнительная информация"
                                    )
                                },
                                shape = RoundedCornerShape(25),
                                label = { Text(text = "Дополнительная информация") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    RoundedSurface {
                        val center by remember {
                            mutableStateOf(LatLng(56.826961, 60.603849))
                        }
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Место и дата пропажи",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = viewModel.createEntity.date,
                                onValueChange = viewModel::setDate,
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.DateRange,
                                        contentDescription = "Дата"
                                    )
                                },
                                shape = RoundedCornerShape(25),
                                label = { Text(text = "Когда потеряли") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            /*
                                                        var text by remember { mutableStateOf("") }

                                                        AnimatedVisibility(
                                                            viewModel.locationAutofill.isNotEmpty(),
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(8.dp)
                                                        ) {
                                                            LazyColumn(
                                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                                            ) {
                                                                items(viewModel.locationAutofill) {
                                                                    Row(
                                                                        modifier = Modifier
                                                                            .fillMaxWidth()
                                                                            .padding(16.dp)
                                                                            .clickable {
                                                                                text = it.address
                                                                                viewModel.locationAutofill.clear()
                                                                            }
                                                                    ) {
                                                                        Text(it.address)
                                                                    }
                                                                }
                                                            }
                                                            */
//                            Spacer(Modifier.height(16.dp))

                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = viewModel.createEntity.geoPosition,
                                onValueChange = viewModel::setGeoPosition,
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.LocationOn,
                                        contentDescription = "Место"
                                    )
                                },
                                shape = RoundedCornerShape(25),
                                label = { Text(text = "Город, улица, дом") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            RoundedSurface {
                                GoogleMaps(
                                    center = center,
                                    modifier = Modifier.aspectRatio(2f)
                                ) {
                                    Marker(
                                        state = rememberMarkerState(position = center)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RadioButtonText(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}

enum class PageType(val urlPrefix: String) {
    ANIMAL_LOST("lostAnimals"), ANIMAL_FOUND("foundAnimals")
}

enum class AnimalType(val localized: String, val localizedMany: String) {
    NONE("Все", "Все"), DOG("Собака", "Собаки"), CAT("Кошка", "Кошки"), OTHER("Другой", "Другие")
}