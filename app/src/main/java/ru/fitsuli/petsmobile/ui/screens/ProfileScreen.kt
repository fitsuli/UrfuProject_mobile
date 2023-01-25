package ru.fitsuli.petsmobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.fitsuli.petsmobile.R
import ru.fitsuli.petsmobile.ui.components.SimpleScaffold

/**
 * Created by Dmitry Danilyuk at 06.12.2022
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToSignIn: () -> Unit,
    viewModel: ProfileScreenViewModel = viewModel()
) {
    SimpleScaffold(
        modifier = modifier,
        headerText = stringResource(id = R.string.profile)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(text = "${viewModel.userInfo}")
            if (viewModel.userInfo == null) {
                Button(onClick = onNavigateToSignIn) {
                    Text(text = "Sign in")
                }

            } else {
                Button(onClick = viewModel::signOut) {
                    Text(text = "Sign out")
                }
            }
            Button(onClick = viewModel::getMe) {
                Text(text = "Get me")
            }
        }
    }
}