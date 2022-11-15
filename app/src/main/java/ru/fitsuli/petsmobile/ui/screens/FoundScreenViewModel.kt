package ru.fitsuli.petsmobile.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.fitsuli.petsmobile.ui.BottomBarTabs

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
class FoundScreenViewModel(application: Application) : AndroidViewModel(application) {
    val tabInfo = BottomBarTabs.FOUND

}