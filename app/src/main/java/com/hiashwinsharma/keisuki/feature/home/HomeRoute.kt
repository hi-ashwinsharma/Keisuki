package com.hiashwinsharma.keisuki.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    onCounterClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is HomeUiAction.OnCounterClick -> onCounterClick(action.counterId)
                is HomeUiAction.OnSettingsClick -> onSettingsClick()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier
    )
}
