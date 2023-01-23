package ru.fitsuli.petsmobile.ui.screens.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import ru.fitsuli.petsmobile.R
import ru.fitsuli.petsmobile.data.dto.Gender
import ru.fitsuli.petsmobile.ui.components.GoogleMaps
import ru.fitsuli.petsmobile.ui.components.PetItem
import ru.fitsuli.petsmobile.ui.components.RoundedSurface
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold

/**
 * Created by Dmitry Danilyuk at 15.11.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onNavigateToPet: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LostScreenViewModel = viewModel() // using LostScreenViewModel here as it is the same
) {
    SimpleScaffold(
        modifier = modifier,
        headerText = stringResource(id = R.string.map)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            RoundedSurface(
                modifier = Modifier
                    .weight(1f)
            ) {

                GoogleMaps(
                    center = LatLng(56.826961, 60.603849),
                ) {
                    viewModel.animalList.forEach { animal ->
                        val latlng by remember {
                            derivedStateOf {
                                val (long, lat) = animal.geoPosition.split(" ")
                                    .map { it.toDouble() }
                                LatLng(lat, long)
                            }
                        }
                        Marker(
                            state = MarkerState(position = latlng),
                            title = "${
                                if (animal.gender == Gender.MALE.value) "Потерялся" else "Потерялась"
                            } ${animal.animalName} \n${animal.addressFull?.takeIf { it.isNotBlank() } ?: animal.addressCity}}",
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(10) {
                    PetItem(
                        title = "Title $it",
                        subtitle = "Subtitle",
                        onClick = { onNavigateToPet() },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}