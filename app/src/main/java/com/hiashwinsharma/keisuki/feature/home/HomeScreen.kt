package com.hiashwinsharma.keisuki.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.R
import com.hiashwinsharma.keisuki.core.designsystem.LocalAppCornerRadius
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.core.model.ColorToken
import com.hiashwinsharma.keisuki.feature.auth.AuthBottomSheet
import com.hiashwinsharma.keisuki.feature.home.components.AddCounterBottomSheet
import com.hiashwinsharma.keisuki.feature.home.components.HomeCounterGrid
import com.hiashwinsharma.keisuki.feature.home.components.HomeEmptyState
import com.hiashwinsharma.keisuki.feature.home.components.HomeTopBar

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAction: (HomeUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    val currentRadius = LocalAppCornerRadius.current

    var showAddDialog by remember { mutableStateOf(false) }
    var showAuthSheet by remember { mutableStateOf(false) }
    val webClientId = stringResource(id = R.string.google_web_client_id)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HomeTopBar(
                authState = uiState.authState,
                isSyncPending = uiState.isSyncPending,
                currentSortOrder = uiState.preferences.sortOrder,
                onSortSelected = { onAction(HomeUiAction.OnSetSortOrder(it)) },
                onAuthClick = { showAuthSheet = true },
                onHistoryClick = { onAction(HomeUiAction.OnHistoryClick) },
                onSettingsClick = { onAction(HomeUiAction.OnSettingsClick) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    haptics.pop()
                    showAddDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(currentRadius * 0.75f),
                elevation = FloatingActionButtonDefaults.elevation(4.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Counter"
                )
            }
        }
    ) { paddingValues ->
        if (uiState.counters.isEmpty()) {
            HomeEmptyState(modifier = Modifier.padding(paddingValues))
        } else {
            HomeCounterGrid(
                counters = uiState.counters,
                gridLayoutMode = uiState.preferences.gridLayout,
                onCounterClick = { onAction(HomeUiAction.OnCounterClick(it)) },
                onIncrement = { id -> onAction(HomeUiAction.OnIncrement(id)) },
                onDecrement = { id -> onAction(HomeUiAction.OnDecrement(id)) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }

    if (showAddDialog) {
        AddCounterBottomSheet(
            defaultColorToken = ColorToken.DYNAMIC_PRIMARY,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, initialCount, step, colorToken ->
                onAction(
                    HomeUiAction.OnCreateCounter(
                        title = title,
                        initialCount = initialCount,
                        step = step,
                        colorToken = colorToken
                    )
                )
                showAddDialog = false
            }
        )
    }

    if (showAuthSheet) {
        AuthBottomSheet(
            authState = uiState.authState,
            isSyncPending = uiState.isSyncPending,
            onDismiss = { showAuthSheet = false },
            onSignInWithGoogle = { onAction(HomeUiAction.OnSignInWithGoogle(webClientId)) },
            onSignOut = { onAction(HomeUiAction.OnSignOut) }
        )
    }
}
