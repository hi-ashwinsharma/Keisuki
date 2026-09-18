package com.hiashwinsharma.keisuki.feature.focus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hiashwinsharma.keisuki.core.designsystem.LocalAppCornerRadius
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.core.model.Counter
import com.hiashwinsharma.keisuki.data.preferences.UserPreferences
import com.hiashwinsharma.keisuki.feature.focus.components.CounterFocusOptionsBottomSheet
import com.hiashwinsharma.keisuki.feature.focus.components.FocusBody
import com.hiashwinsharma.keisuki.feature.focus.components.FocusTopBar

@Composable
fun FocusScreen(
    counter: Counter?,
    preferences: UserPreferences,
    onAction: (FocusUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    val currentRadius = LocalAppCornerRadius.current

    var showOptionsSheet by remember { mutableStateOf(false) }
    var isEditingTitle by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf("") }

    val currentCounter = counter

    var previousCount by remember { mutableStateOf<Long?>(null) }
    LaunchedEffect(currentCounter?.count, preferences.milestoneCelebrationEnabled) {
        val count = currentCounter?.count ?: return@LaunchedEffect
        if (previousCount != null && count != previousCount) {
            if (preferences.milestoneCelebrationEnabled && count > 0 && (count % 100L == 0L || count % 50L == 0L || count % 10L == 0L)) {
                haptics.milestone()
            }
        }
        previousCount = count
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            FocusTopBar(
                counter = currentCounter,
                isEditingTitle = isEditingTitle,
                editedTitle = editedTitle,
                onTitleChange = { editedTitle = it },
                onStartEditTitle = {
                    editedTitle = currentCounter?.title ?: ""
                    isEditingTitle = true
                },
                onSaveTitle = {
                    if (editedTitle.isNotBlank() && currentCounter != null) {
                        onAction(
                            FocusUiAction.OnUpdateCounter(
                                title = editedTitle,
                                count = currentCounter.count,
                                step = currentCounter.step,
                                colorToken = currentCounter.colorToken
                            )
                        )
                    }
                    isEditingTitle = false
                },
                onBackClick = { onAction(FocusUiAction.OnBackClick) },
                onHistoryClick = { onAction(FocusUiAction.OnHistoryClick) },
                onOptionsClick = { showOptionsSheet = true }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (currentCounter == null) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                FocusBody(
                    counter = currentCounter,
                    preferences = preferences,
                    cornerRadius = currentRadius,
                    onAction = onAction
                )
            }
        }
    }

    if (showOptionsSheet && currentCounter != null) {
        CounterFocusOptionsBottomSheet(
            counter = currentCounter,
            onDismiss = { showOptionsSheet = false },
            onColorChanged = { newColor ->
                onAction(
                    FocusUiAction.OnUpdateCounter(
                        title = currentCounter.title,
                        count = currentCounter.count,
                        step = currentCounter.step,
                        colorToken = newColor
                    )
                )
            },
            onReset = { onAction(FocusUiAction.OnReset) },
            onDelete = {
                onAction(FocusUiAction.OnDelete(onDeleted = { onAction(FocusUiAction.OnBackClick) }))
            }
        )
    }
}
