package ru.fitsuli.petsmobile.ui.screens

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.launch
import ru.fitsuli.petsmobile.data.dto.SignUpEntity
import ru.fitsuli.petsmobile.ui.BaseViewModel
import timber.log.Timber

/**
 * Created by Dmitry Danilyuk at 06.12.2022
 */
class SignInScreenViewModel(application: Application) : BaseViewModel(application) {

    var isSignUpSuccessful by mutableStateOf<Boolean?>(null)
        private set

    var isSignInSuccessful by mutableStateOf<Boolean?>(null)
        private set

    fun signIn(login: String, password: String) {
        viewModelScope.launch {
            apiClient.signIn(login, password)
                .suspendOnSuccess {
                    Timber.e("signIn: $data")
                    isSignInSuccessful = true
                }
                .suspendOnFailure {
                    Timber.e("signIn: ${message()}")
                    isSignInSuccessful = false
                }
        }
    }

    fun signUp(signUp: SignUpEntity) {
        viewModelScope.launch {
            apiClient.signUp(signUp)
                .suspendOnSuccess {
                    Timber.d("signUp: $data")
                    isSignUpSuccessful = true
                }.suspendOnFailure {
                    Timber.d("signUp: ${message()}")
                    isSignUpSuccessful = false
                }
        }
    }
}