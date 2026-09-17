package com.hiashwinsharma.keisuki.feature.focus

import android.app.Activity
import android.view.KeyEvent
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics

@Composable
fun FocusRoute(
    viewModel: FocusViewModel,
    onBack: () -> Unit,
    onSetKeyHandler: (((Int) -> Boolean)?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val counter by viewModel.counterFlow.collectAsStateWithLifecycle()
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val haptics = rememberExpressiveHaptics()
    val context = LocalContext.current

    // Keep Screen Awake (WakeLock)
    DisposableEffect(preferences.keepScreenAwake) {
        val window = (context as? Activity)?.window
        if (preferences.keepScreenAwake) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    // Register hardware volume key listeners
    DisposableEffect(counter, preferences.volumeKeysEnabled) {
        onSetKeyHandler { keyCode ->
            if (!preferences.volumeKeysEnabled || counter == null) return@onSetKeyHandler false
            when (keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    haptics.tick()
                    viewModel.increment()
                    true
                }
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    haptics.tick()
                    viewModel.decrement()
                    true
                }
                else -> false
            }
        }
        onDispose {
            onSetKeyHandler(null)
        }
    }

    FocusScreen(
        counter = counter,
        preferences = preferences,
        onAction = { action ->
            when (action) {
                is FocusUiAction.OnBackClick -> onBack()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier
    )
}
