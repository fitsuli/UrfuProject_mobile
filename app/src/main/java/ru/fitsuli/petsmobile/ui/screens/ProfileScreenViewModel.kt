package ru.fitsuli.petsmobile.ui.screens

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.getOrNull
import kotlinx.coroutines.launch
import ru.fitsuli.petsmobile.data.dto.UserEntity
import ru.fitsuli.petsmobile.ui.BaseViewModel
import ru.fitsuli.petsmobile.ui.BottomBarTabs
import ru.fitsuli.petsmobile.utils.removeCookiePref
import timber.log.Timber

/**
 * Created by Dmitry Danilyuk at 06.12.2022
 */
class ProfileScreenViewModel(application: Application) : BaseViewModel(application) {
    val tabInfo = BottomBarTabs.PROFILE

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

    fun signOut() {
        viewModelScope.launch {
            Timber.d("signOut()")
            context.removeCookiePref()
            userInfo = null
        }
    }
}