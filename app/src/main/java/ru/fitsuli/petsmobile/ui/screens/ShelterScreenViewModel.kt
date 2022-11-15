package ru.fitsuli.petsmobile.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.fitsuli.petsmobile.ui.BottomBarTabs

/**
 * Created by Dmitry Danilyuk at 15.11.2022
 */
class ShelterScreenViewModel(application: Application) : AndroidViewModel(application) {
    val tabInfo = BottomBarTabs.SHELTER

    fun onItemClicked() {

    }
}