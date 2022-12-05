package ru.fitsuli.petsmobile.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.fitsuli.petsmobile.data.dto.SignUpEntity
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold

/**
 * Created by Dmitry Danilyuk at 06.12.2022
 */
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInScreenViewModel = viewModel()
) {
    var page by remember { mutableStateOf(SignInPages.SIGN_IN) }

    SimpleScaffold(
        modifier = modifier,
        headerText = "Sign in"
    ) { paddingValues ->
        AnimatedContent(
            targetState = page,
            modifier = Modifier.padding(paddingValues)
        ) { pages ->
            when (pages) {
                SignInPages.SIGN_IN -> SignInPage(
                    onSignUpClick = { page = SignInPages.SIGN_UP },
                    onSignInAction = viewModel::signIn
                )

                SignInPages.SIGN_UP -> SignUpPage(
                    onSignInOpen = { page = SignInPages.SIGN_IN },
                    onSignUpAction = viewModel::signUp,
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInPage(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    onSignInAction: (login: String, password: String) -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // TODO: basic validation
        var login by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        TextField(
            value = login,
            onValueChange = { login = it },
            label = { Text(text = "Login") },
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
        )
        Button(onClick = { onSignInAction(login, password) }) {
            Text(text = "Sign in")
        }
        Button(onClick = onSignUpClick) {
            Text(text = "Sign up")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpPage(
    modifier: Modifier = Modifier,
    onSignInOpen: () -> Unit,
    onSignUpAction: (SignUpEntity) -> Unit
) {
    BackHandler(onBack = onSignInOpen)
    var signUp by remember { mutableStateOf(SignUpEntity()) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically)
    ) {
        TextField(
            value = signUp.fullName,
            onValueChange = { signUp = signUp.copy(fullName = it) },
            label = { Text(text = "Full name") }
        )
        TextField(
            value = signUp.login,
            onValueChange = { signUp = signUp.copy(login = it) },
            label = { Text(text = "Login") }
        )
        TextField(
            value = signUp.password,
            onValueChange = { signUp = signUp.copy(password = it) },
            label = { Text(text = "Password") }
        )
        Button(onClick = { onSignUpAction(signUp) }) {
            Text(text = "Sign up")
        }
    }
}

private enum class SignInPages {
    SIGN_IN,
    SIGN_UP
}