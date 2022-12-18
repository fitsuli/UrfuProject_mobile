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

    var viewState by mutableStateOf(SignInScreenState())
        private set

    fun signIn(login: String, password: String) {
        viewModelScope.launch {
            viewState = viewState.copy(isLoading = true)
            apiClient.signIn(login, password)
                .suspendOnSuccess {
                    Timber.d("signIn() success")
                    viewState = viewState.copy(isSignInSuccessful = true)
                }
                .suspendOnFailure {
                    Timber.d("signIn() failed: ${message()}")
                    viewState = viewState.copy(isSignInSuccessful = false)
                }

            viewState = viewState.copy(isLoading = false)
        }
    }

    fun signUp(signUp: SignUpEntity) {
        viewModelScope.launch {
            viewState = viewState.copy(isLoading = true)
            apiClient.signUp(signUp)
                .suspendOnSuccess {
                    Timber.d("signUp() success")
                    viewState = viewState.copy(isSignUpSuccessful = true)
                }.suspendOnFailure {
                    Timber.d("signUp() failed: ${message()}")
                    viewState = viewState.copy(isSignUpSuccessful = false)
                }

            viewState = viewState.copy(isLoading = false)
        }
    }
}

data class SignInScreenState(
    val isLoading: Boolean = false,
    val isUserSignedIn: Boolean = false,
    val isSignInSuccessful: Boolean? = null,
    val isSignUpSuccessful: Boolean? = null,
)
