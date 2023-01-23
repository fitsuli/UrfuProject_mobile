package ru.fitsuli.petsmobile.ui.screens.feed

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.launch
import ru.fitsuli.petsmobile.data.dto.AnimalEntity
import ru.fitsuli.petsmobile.ui.BaseViewModel
import timber.log.Timber

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
class LostScreenViewModel(application: Application) : BaseViewModel(application) {

    var animalList by mutableStateOf(emptyList<AnimalEntity>())
        private set

    fun getLostAnimals() {
        viewModelScope.launch {
            apiClient.getLostPets()
                .onSuccess {
                    Timber.d("getLostAnimals() success")
                    animalList = data
                }.onFailure {
                    Timber.d("getLostAnimals() failed: ${message()}")
                }
        }
    }

    init {
        getLostAnimals()
    }
}