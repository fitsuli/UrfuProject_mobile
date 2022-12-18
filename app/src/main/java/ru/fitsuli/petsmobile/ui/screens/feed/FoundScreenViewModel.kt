package ru.fitsuli.petsmobile.ui.screens.feed

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.fitsuli.petsmobile.ui.BaseViewModel
import ru.fitsuli.petsmobile.ui.BottomBarTabs

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
class FoundScreenViewModel(application: Application) : BaseViewModel(application) {
    val tabInfo = BottomBarTabs.FOUND

    fun getFoundAnimals() {
        viewModelScope.launch {

        }
    }
}