package com.hiashwinsharma.keisuki.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.hiashwinsharma.keisuki.data.auth.AuthState
import com.hiashwinsharma.keisuki.data.preferences.UserPreferences
import com.hiashwinsharma.keisuki.feature.auth.AuthBottomSheet
import com.hiashwinsharma.keisuki.feature.settings.components.AccountDataSettingsSection
import com.hiashwinsharma.keisuki.feature.settings.components.CornerRadiusSettingsSection
import com.hiashwinsharma.keisuki.feature.settings.components.ErgonomicsSettingsSection
import com.hiashwinsharma.keisuki.feature.settings.components.HapticsSettingsSection
import com.hiashwinsharma.keisuki.feature.settings.components.HomeLayoutSettingsSection
import com.hiashwinsharma.keisuki.feature.settings.components.ResetAllCountersBottomSheet
import com.hiashwinsharma.keisuki.feature.settings.components.SettingsTopBar
import com.hiashwinsharma.keisuki.feature.settings.components.ThemeSettingsSection

@Composable
fun SettingsScreen(
    preferences: UserPreferences,
    authState: AuthState,
    onAction: (SettingsUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentRadius = LocalAppCornerRadius.current
    var showResetSheet by remember { mutableStateOf(false) }
    var showAuthSheet by remember { mutableStateOf(false) }
    val webClientId = stringResource(id = R.string.google_web_client_id)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SettingsTopBar(onBackClick = { onAction(SettingsUiAction.OnBackClick) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. APPEARANCE & THEMING
            item {
                ThemeSettingsSection(
                    currentThemeMode = preferences.themeMode,
                    isDynamicColor = preferences.isDynamicColor,
                    cornerRadius = currentRadius,
                    onSetThemeMode = { onAction(SettingsUiAction.SetThemeMode(it)) },
                    onSetDynamicColor = { onAction(SettingsUiAction.SetDynamicColor(it)) }
                )
            }

            // 2. CORNER GEOMETRY & SHAPES
            item {
                CornerRadiusSettingsSection(
                    cornerRadiusDp = preferences.cornerRadiusDp,
                    isPillButtons = preferences.isPillButtons,
                    cornerRadius = currentRadius,
                    onSetCornerRadius = { onAction(SettingsUiAction.SetCornerRadius(it)) },
                    onSetPillButtons = { onAction(SettingsUiAction.SetPillButtons(it)) }
                )
            }

            // 3. ERGONOMICS & CONTROLS
            item {
                ErgonomicsSettingsSection(
                    isLeftHanded = preferences.isLeftHanded,
                    volumeKeysEnabled = preferences.volumeKeysEnabled,
                    keepScreenAwake = preferences.keepScreenAwake,
                    swipeGesturesEnabled = preferences.swipeGesturesEnabled,
                    cornerRadius = currentRadius,
                    onSetLeftHanded = { onAction(SettingsUiAction.SetLeftHanded(it)) },
                    onSetVolumeKeysEnabled = { onAction(SettingsUiAction.SetVolumeKeysEnabled(it)) },
                    onSetKeepScreenAwake = { onAction(SettingsUiAction.SetKeepScreenAwake(it)) },
                    onSetSwipeGesturesEnabled = { onAction(SettingsUiAction.SetSwipeGesturesEnabled(it)) }
                )
            }

            // 4. HAPTICS & ACCELERATION
            item {
                HapticsSettingsSection(
                    hapticIntensity = preferences.hapticIntensity,
                    milestoneCelebrationEnabled = preferences.milestoneCelebrationEnabled,
                    rapidHoldSpeed = preferences.rapidHoldSpeed,
                    cornerRadius = currentRadius,
                    onSetHapticIntensity = { onAction(SettingsUiAction.SetHapticIntensity(it)) },
                    onSetMilestoneCelebrationEnabled = { onAction(SettingsUiAction.SetMilestoneCelebrationEnabled(it)) },
                    onSetRapidHoldSpeed = { onAction(SettingsUiAction.SetRapidHoldSpeed(it)) }
                )
            }

            // 5. HOME GRID STYLE
            item {
                HomeLayoutSettingsSection(
                    gridLayout = preferences.gridLayout,
                    sortOrder = preferences.sortOrder,
                    cornerRadius = currentRadius,
                    onSetGridLayout = { onAction(SettingsUiAction.SetGridLayout(it)) },
                    onSetSortOrder = { onAction(SettingsUiAction.SetSortOrder(it)) }
                )
            }

            // 6. ACCOUNT & DATA
            item {
                AccountDataSettingsSection(
                    authState = authState,
                    cornerRadius = currentRadius,
                    onAuthClick = { showAuthSheet = true },
                    onResetAllClick = { showResetSheet = true }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showResetSheet) {
        ResetAllCountersBottomSheet(
            cornerRadius = currentRadius,
            onConfirmReset = { onAction(SettingsUiAction.ResetAllCounters) },
            onDismiss = { showResetSheet = false }
        )
    }

    if (showAuthSheet) {
        AuthBottomSheet(
            authState = authState,
            isSyncPending = false,
            onDismiss = { showAuthSheet = false },
            onSignInWithGoogle = { onAction(SettingsUiAction.SignInWithGoogle(webClientId)) },
            onSignOut = { onAction(SettingsUiAction.SignOut) }
        )
    }
}
