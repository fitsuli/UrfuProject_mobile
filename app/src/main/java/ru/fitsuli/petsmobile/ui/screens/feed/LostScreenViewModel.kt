package ru.fitsuli.petsmobile.ui.screens.feed

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fitsuli.petsmobile.data.dto.AnimalEntity
import ru.fitsuli.petsmobile.ui.BaseViewModel
import ru.fitsuli.petsmobile.ui.screens.AnimalType
import timber.log.Timber

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
class LostScreenViewModel(application: Application) : BaseViewModel(application) {

    private var animalList = MutableStateFlow(emptyList<AnimalEntity>())

    private var filterAnimalType = MutableStateFlow("")

    var sortByDescendingDate = MutableStateFlow(true)

    var scrollToTop by mutableStateOf(false)

    private val filtered = combine(filterAnimalType, animalList) { typeFilter, list ->
        if (typeFilter.isBlank() || typeFilter == AnimalType.NONE.localized) return@combine list
        if (typeFilter == AnimalType.OTHER.localized) return@combine list.filter {
            it.animalType != AnimalType.CAT.localized &&
                    it.animalType != AnimalType.DOG.localized
        }
        list.filter { it.animalType == typeFilter }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val sorted = combine(filtered, sortByDescendingDate) { list, isDescending ->
        if (isDescending) list.sortedByDescending { it.date }
        else list.sortedBy { it.date }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun setFilterAnimalType(type: String) {
        filterAnimalType.update { type }
    }

    fun setSortByDescendingDate(isDescending: Boolean) {
        sortByDescendingDate.update { isDescending }
    }

    fun toggleSortByDescendingDate() {
        sortByDescendingDate.update { !sortByDescendingDate.value }
    }

    fun getLostAnimals() {
        viewModelScope.launch {
            apiClient.getLostPets()
                .onSuccess {
                    Timber.d("getLostAnimals() success")
                    animalList.update { data }
                }.onFailure {
                    Timber.d("getLostAnimals() failed: ${message()}")
                }
        }
    }

    init {
        getLostAnimals()
    }
}