package ru.fitsuli.petsmobile.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.getOrNull
import kotlinx.coroutines.launch
import ru.fitsuli.petsmobile.data.dto.UserEntity
import ru.fitsuli.petsmobile.utils.removeCookiePref
import timber.log.Timber

/**
 * Created by Dmitry Danilyuk at 23.01.2023
 */
class GlobalViewModel(application: Application) : BaseViewModel(application) {
    var userInfo by mutableStateOf<UserEntity?>(null)
        private set

    fun getMe() {
        viewModelScope.launch {
            apiClient.me().getOrNull().let {
                userInfo = it
                Timber.d("me: $it")
            }
        }
    }

    init {
        getMe()
    }

    fun signOut() {
        viewModelScope.launch {
            Timber.d("signOut()")
            context.removeCookiePref()
            userInfo = null
        }
    }
}