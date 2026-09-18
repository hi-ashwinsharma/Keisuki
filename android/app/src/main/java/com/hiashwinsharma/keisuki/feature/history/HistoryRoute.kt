package com.hiashwinsharma.keisuki.feature.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HistoryRoute(
    viewModel: HistoryViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is HistoryUiAction.OnBackClick -> onBack()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier
    )
}
