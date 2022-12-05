package ru.fitsuli.petsmobile.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.fitsuli.petsmobile.App

/**
 * Created by Dmitry Danilyuk at 22.11.2022
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val apiClient = (application as App).apiClient

}