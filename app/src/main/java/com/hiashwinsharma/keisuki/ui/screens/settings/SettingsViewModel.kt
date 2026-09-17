package com.hiashwinsharma.keisuki.ui.screens.settings

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
                counterRepository.onUserSignedIn(user.uid)
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
