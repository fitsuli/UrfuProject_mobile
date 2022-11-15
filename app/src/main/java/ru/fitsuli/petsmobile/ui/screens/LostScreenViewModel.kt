package ru.fitsuli.petsmobile.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.fitsuli.petsmobile.ui.BottomBarTabs

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */
class LostScreenViewModel(application: Application) : AndroidViewModel(application) {
    val tabInfo = BottomBarTabs.LOST

}