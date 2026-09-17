package com.hiashwinsharma.keisuki.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _preferences = MutableStateFlow(loadPreferences())
    val preferences: StateFlow<UserPreferences> = _preferences.asStateFlow()

    private fun loadPreferences(): UserPreferences {
        val themeModeStr = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        val themeMode = runCatching { ThemeMode.valueOf(themeModeStr) }.getOrDefault(ThemeMode.SYSTEM)

        val isDynamicColor = prefs.getBoolean(KEY_DYNAMIC_COLOR, true)
        val cornerRadiusDp = prefs.getFloat(KEY_CORNER_RADIUS, 24f)

        val gridLayoutStr = prefs.getString(KEY_GRID_LAYOUT, GridLayoutMode.TWO_COLUMNS.name) ?: GridLayoutMode.TWO_COLUMNS.name
        val gridLayout = runCatching { GridLayoutMode.valueOf(gridLayoutStr) }.getOrDefault(GridLayoutMode.TWO_COLUMNS)

        val isLeftHanded = prefs.getBoolean(KEY_LEFT_HANDED, false)
        val volumeKeysEnabled = prefs.getBoolean(KEY_VOLUME_KEYS, true)
        val keepScreenAwake = prefs.getBoolean(KEY_KEEP_AWAKE, false)
        val swipeGesturesEnabled = prefs.getBoolean(KEY_SWIPE_GESTURES, true)

        val hapticStr = prefs.getString(KEY_HAPTIC_INTENSITY, HapticIntensity.MEDIUM.name) ?: HapticIntensity.MEDIUM.name
        val hapticIntensity = runCatching { HapticIntensity.valueOf(hapticStr) }.getOrDefault(HapticIntensity.MEDIUM)

        val milestoneEnabled = prefs.getBoolean(KEY_MILESTONE_HAPTICS, true)

        val speedStr = prefs.getString(KEY_RAPID_SPEED, RapidHoldSpeed.FAST.name) ?: RapidHoldSpeed.FAST.name
        val rapidSpeed = runCatching { RapidHoldSpeed.valueOf(speedStr) }.getOrDefault(RapidHoldSpeed.FAST)

        val isPillButtons = prefs.getBoolean(KEY_PILL_BUTTONS, false)

        val sortOrderStr = prefs.getString(KEY_SORT_ORDER, CounterSortOrder.LATEST_CHANGE.name) ?: CounterSortOrder.LATEST_CHANGE.name
        val sortOrder = runCatching { CounterSortOrder.valueOf(sortOrderStr) }.getOrDefault(CounterSortOrder.LATEST_CHANGE)

        return UserPreferences(
            themeMode = themeMode,
            isDynamicColor = isDynamicColor,
            cornerRadiusDp = cornerRadiusDp,
            gridLayout = gridLayout,
            isLeftHanded = isLeftHanded,
            volumeKeysEnabled = volumeKeysEnabled,
            keepScreenAwake = keepScreenAwake,
            swipeGesturesEnabled = swipeGesturesEnabled,
            hapticIntensity = hapticIntensity,
            milestoneCelebrationEnabled = milestoneEnabled,
            rapidHoldSpeed = rapidSpeed,
            isPillButtons = isPillButtons,
            sortOrder = sortOrder
        )
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
        _preferences.value = _preferences.value.copy(themeMode = mode)
    }

    fun setDynamicColor(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DYNAMIC_COLOR, enabled).apply()
        _preferences.value = _preferences.value.copy(isDynamicColor = enabled)
    }

    fun setCornerRadius(radiusDp: Float) {
        val coerced = radiusDp.coerceIn(0f, 36f)
        prefs.edit().putFloat(KEY_CORNER_RADIUS, coerced).apply()
        _preferences.value = _preferences.value.copy(cornerRadiusDp = coerced)
    }

    fun setGridLayout(layout: GridLayoutMode) {
        prefs.edit().putString(KEY_GRID_LAYOUT, layout.name).apply()
        _preferences.value = _preferences.value.copy(gridLayout = layout)
    }

    fun setLeftHanded(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_LEFT_HANDED, enabled).apply()
        _preferences.value = _preferences.value.copy(isLeftHanded = enabled)
    }

    fun setVolumeKeysEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_VOLUME_KEYS, enabled).apply()
        _preferences.value = _preferences.value.copy(volumeKeysEnabled = enabled)
    }

    fun setKeepScreenAwake(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_KEEP_AWAKE, enabled).apply()
        _preferences.value = _preferences.value.copy(keepScreenAwake = enabled)
    }

    fun setSwipeGesturesEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SWIPE_GESTURES, enabled).apply()
        _preferences.value = _preferences.value.copy(swipeGesturesEnabled = enabled)
    }

    fun setHapticIntensity(intensity: HapticIntensity) {
        prefs.edit().putString(KEY_HAPTIC_INTENSITY, intensity.name).apply()
        _preferences.value = _preferences.value.copy(hapticIntensity = intensity)
    }

    fun setMilestoneCelebrationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_MILESTONE_HAPTICS, enabled).apply()
        _preferences.value = _preferences.value.copy(milestoneCelebrationEnabled = enabled)
    }

    fun setRapidHoldSpeed(speed: RapidHoldSpeed) {
        prefs.edit().putString(KEY_RAPID_SPEED, speed.name).apply()
        _preferences.value = _preferences.value.copy(rapidHoldSpeed = speed)
    }

    fun setPillButtons(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PILL_BUTTONS, enabled).apply()
        _preferences.value = _preferences.value.copy(isPillButtons = enabled)
    }

    fun setSortOrder(sortOrder: CounterSortOrder) {
        prefs.edit().putString(KEY_SORT_ORDER, sortOrder.name).apply()
        _preferences.value = _preferences.value.copy(sortOrder = sortOrder)
    }

    companion object {
        private const val PREFS_NAME = "keisuki_user_preferences"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_DYNAMIC_COLOR = "dynamic_color"
        private const val KEY_CORNER_RADIUS = "corner_radius"
        private const val KEY_GRID_LAYOUT = "grid_layout"
        private const val KEY_LEFT_HANDED = "left_handed"
        private const val KEY_VOLUME_KEYS = "volume_keys"
        private const val KEY_KEEP_AWAKE = "keep_awake"
        private const val KEY_SWIPE_GESTURES = "swipe_gestures"
        private const val KEY_HAPTIC_INTENSITY = "haptic_intensity"
        private const val KEY_MILESTONE_HAPTICS = "milestone_haptics"
        private const val KEY_RAPID_SPEED = "rapid_speed"
        private const val KEY_PILL_BUTTONS = "pill_buttons"
        private const val KEY_SORT_ORDER = "sort_order"
    }
}
