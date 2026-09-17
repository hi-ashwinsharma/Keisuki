package com.hiashwinsharma.keisuki.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiashwinsharma.keisuki.data.auth.AuthRepository
import com.hiashwinsharma.keisuki.data.auth.AuthState
import com.hiashwinsharma.keisuki.data.preferences.CounterSortOrder
import com.hiashwinsharma.keisuki.data.preferences.GridLayoutMode
import com.hiashwinsharma.keisuki.data.preferences.HapticIntensity
import com.hiashwinsharma.keisuki.data.preferences.RapidHoldSpeed
import com.hiashwinsharma.keisuki.data.preferences.ThemeMode
import com.hiashwinsharma.keisuki.data.preferences.UserPreferences
import com.hiashwinsharma.keisuki.data.preferences.UserPreferencesRepository
import com.hiashwinsharma.keisuki.data.repository.CounterRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val counterRepository: CounterRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = preferencesRepository.preferences
    val authState: StateFlow<AuthState> = authRepository.authState

    fun onAction(action: SettingsUiAction) {
        when (action) {
            is SettingsUiAction.SetThemeMode -> setThemeMode(action.mode)
            is SettingsUiAction.SetDynamicColor -> setDynamicColor(action.enabled)
            is SettingsUiAction.SetCornerRadius -> setCornerRadius(action.radiusDp)
            is SettingsUiAction.SetGridLayout -> setGridLayout(action.layout)
            is SettingsUiAction.SetLeftHanded -> setLeftHanded(action.enabled)
            is SettingsUiAction.SetVolumeKeysEnabled -> setVolumeKeysEnabled(action.enabled)
            is SettingsUiAction.SetKeepScreenAwake -> setKeepScreenAwake(action.enabled)
            is SettingsUiAction.SetSwipeGesturesEnabled -> setSwipeGesturesEnabled(action.enabled)
            is SettingsUiAction.SetHapticIntensity -> setHapticIntensity(action.intensity)
            is SettingsUiAction.SetMilestoneCelebrationEnabled -> setMilestoneCelebrationEnabled(action.enabled)
            is SettingsUiAction.SetRapidHoldSpeed -> setRapidHoldSpeed(action.speed)
            is SettingsUiAction.SetPillButtons -> setPillButtons(action.enabled)
            is SettingsUiAction.SetSortOrder -> setSortOrder(action.order)
            is SettingsUiAction.ResetAllCounters -> resetAllCounters()
            is SettingsUiAction.SignInWithGoogle -> signInWithGoogle(action.webClientId)
            is SettingsUiAction.SignOut -> signOut()
            is SettingsUiAction.OnBackClick -> Unit
        }
    }

    fun setThemeMode(mode: ThemeMode) = preferencesRepository.setThemeMode(mode)
    fun setDynamicColor(enabled: Boolean) = preferencesRepository.setDynamicColor(enabled)
    fun setCornerRadius(radiusDp: Float) = preferencesRepository.setCornerRadius(radiusDp)
    fun setGridLayout(layout: GridLayoutMode) = preferencesRepository.setGridLayout(layout)
    fun setLeftHanded(enabled: Boolean) = preferencesRepository.setLeftHanded(enabled)
    fun setVolumeKeysEnabled(enabled: Boolean) = preferencesRepository.setVolumeKeysEnabled(enabled)
    fun setKeepScreenAwake(enabled: Boolean) = preferencesRepository.setKeepScreenAwake(enabled)
    fun setSwipeGesturesEnabled(enabled: Boolean) = preferencesRepository.setSwipeGesturesEnabled(enabled)
    fun setHapticIntensity(intensity: HapticIntensity) = preferencesRepository.setHapticIntensity(intensity)
    fun setMilestoneCelebrationEnabled(enabled: Boolean) = preferencesRepository.setMilestoneCelebrationEnabled(enabled)
    fun setRapidHoldSpeed(speed: RapidHoldSpeed) = preferencesRepository.setRapidHoldSpeed(speed)
    fun setPillButtons(enabled: Boolean) = preferencesRepository.setPillButtons(enabled)
    fun setSortOrder(order: CounterSortOrder) = preferencesRepository.setSortOrder(order)

    fun resetAllCounters() {
        viewModelScope.launch {
            counterRepository.resetAllCounters()
        }
    }

    fun signInWithGoogle(webClientId: String) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(webClientId)
            result.onSuccess { user ->
                counterRepository.associateAnonymousDataOnSignIn(user.uid)
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
