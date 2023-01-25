package ru.fitsuli.petsmobile.ui.screens.feed

import android.app.Application
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import ru.fitsuli.petsmobile.data.dto.AnimalEntity
import ru.fitsuli.petsmobile.ui.BaseViewModel
import ru.fitsuli.petsmobile.ui.screens.PageType
import timber.log.Timber

/**
 * Created by Dmitry Danilyuk at 15.11.2022
 */
class MapScreenViewModel(application: Application) : BaseViewModel(application) {

    var animals = mutableStateMapOf<PageType, List<AnimalEntity>>()
        private set


    private fun getLostAnimals() {
        viewModelScope.launch {
            apiClient.getLostPets()
                .onSuccess {
                    Timber.d("getLostAnimals() success")
                    animals[PageType.ANIMAL_LOST] = data
                }.onFailure {
                    Timber.d("getLostAnimals() failed: ${message()}")
                }
        }
    }

    private fun getFoundAnimals() {
        viewModelScope.launch {
            apiClient.getFoundPets()
                .onSuccess {
                    Timber.d("getFoundAnimals() success")
                    animals[PageType.ANIMAL_FOUND] = data
                }.onFailure {
                    Timber.d("getFoundAnimals() failed: ${message()}")
                }
        }
    }

    fun fetchAll() {
        getLostAnimals()
        getFoundAnimals()
    }

    init {
        fetchAll()
    }

}

data class AutocompleteResult(
    val address: String,
    val placeId: String
)