package com.hiashwinsharma.keisuki.feature.settings

import com.hiashwinsharma.keisuki.data.preferences.CounterSortOrder
import com.hiashwinsharma.keisuki.data.preferences.GridLayoutMode
import com.hiashwinsharma.keisuki.data.preferences.HapticIntensity
import com.hiashwinsharma.keisuki.data.preferences.RapidHoldSpeed
import com.hiashwinsharma.keisuki.data.preferences.ThemeMode

sealed interface SettingsUiAction {
    data class SetThemeMode(val mode: ThemeMode) : SettingsUiAction
    data class SetDynamicColor(val enabled: Boolean) : SettingsUiAction
    data class SetCornerRadius(val radiusDp: Float) : SettingsUiAction
    data class SetGridLayout(val layout: GridLayoutMode) : SettingsUiAction
    data class SetLeftHanded(val enabled: Boolean) : SettingsUiAction
    data class SetVolumeKeysEnabled(val enabled: Boolean) : SettingsUiAction
    data class SetKeepScreenAwake(val enabled: Boolean) : SettingsUiAction
    data class SetSwipeGesturesEnabled(val enabled: Boolean) : SettingsUiAction
    data class SetHapticIntensity(val intensity: HapticIntensity) : SettingsUiAction
    data class SetMilestoneCelebrationEnabled(val enabled: Boolean) : SettingsUiAction
    data class SetRapidHoldSpeed(val speed: RapidHoldSpeed) : SettingsUiAction
    data class SetPillButtons(val enabled: Boolean) : SettingsUiAction
    data class SetSortOrder(val order: CounterSortOrder) : SettingsUiAction
    data object ResetAllCounters : SettingsUiAction
    data class SignInWithGoogle(val webClientId: String) : SettingsUiAction
    data object SignOut : SettingsUiAction
    data object OnBackClick : SettingsUiAction
}
