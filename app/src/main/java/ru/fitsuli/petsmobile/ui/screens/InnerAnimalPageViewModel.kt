package ru.fitsuli.petsmobile.ui.screens

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.getOrNull
import kotlinx.coroutines.launch
import ru.fitsuli.petsmobile.data.dto.AnimalEntity
import ru.fitsuli.petsmobile.ui.BaseViewModel

/**
 * Created by Dmitry Danilyuk at 19.12.2022
 */
class InnerAnimalPageViewModel(application: Application) : BaseViewModel(application) {

    var animal by mutableStateOf<AnimalEntity?>(null)

    fun loadAnimal(animalId: String) {
        viewModelScope.launch {
            animal = apiClient.getLostPetById(animalId).getOrNull()
        }
    }
}