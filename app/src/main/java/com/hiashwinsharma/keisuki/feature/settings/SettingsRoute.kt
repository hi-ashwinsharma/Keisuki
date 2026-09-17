package com.hiashwinsharma.keisuki.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    SettingsScreen(
        preferences = preferences,
        authState = authState,
        onAction = { action ->
            when (action) {
                is SettingsUiAction.OnBackClick -> onBack()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier
    )
}
