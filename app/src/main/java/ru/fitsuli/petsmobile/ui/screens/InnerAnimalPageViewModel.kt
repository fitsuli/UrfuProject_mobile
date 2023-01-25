package ru.fitsuli.petsmobile.ui.screens

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import ru.fitsuli.petsmobile.data.dto.AnimalEntity
import ru.fitsuli.petsmobile.ui.BaseViewModel
import timber.log.Timber

/**
 * Created by Dmitry Danilyuk at 19.12.2022
 */
class InnerAnimalPageViewModel(application: Application) : BaseViewModel(application) {

    var animal by mutableStateOf<AnimalEntity?>(null)

    fun loadAnimal(
        animalId: String,
        pageType: PageType
    ) {
        viewModelScope.launch {
            animal = if (pageType == PageType.ANIMAL_LOST) {
                apiClient.getLostPetById(animalId)
            } else {
                apiClient.getFoundPetById(animalId)
            }.getOrNull()
        }
    }

    fun closePost(
        pageType: PageType,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            animal?.let { animalEntity ->
                if (pageType == PageType.ANIMAL_LOST) {
                    apiClient.closeLostPost(animalEntity.id)
                } else {
                    apiClient.closeFoundPost(animalEntity.id)
                }.onSuccess {
                    onSuccess()
                }.onFailure {
                    Timber.d("closePost() failed: ${message()}")
                }
            }
            animal = null
        }
    }
}