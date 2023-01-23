package ru.fitsuli.petsmobile

import android.app.Application
import ru.fitsuli.petsmobile.data.network.PetApi
import ru.fitsuli.petsmobile.utils.newRetrofit
import timber.log.Timber

/**
 * Created by Dmitry Danilyuk at 22.11.2022
 */
class App : Application() {

    val apiClient by lazy {
        newRetrofit(this, "http://localhost:7257/api/", PetApi::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        // TODO
        Timber.plant(Timber.DebugTree())
    }
}

private const val GOOGLE_MAP_API_KEY = "AIzaSyDqpSB-Pg3C-BzIr3q2HTh882lIwhnAS2U"