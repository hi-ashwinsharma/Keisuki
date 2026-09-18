package com.hiashwinsharma.keisuki.data.preferences

enum class ThemeMode(val title: String) {
    SYSTEM("System"),
    LIGHT("Light"),
    DARK("Dark"),
    AMOLED("AMOLED")
}

enum class GridLayoutMode(val title: String) {
    TWO_COLUMNS("2 Columns"),
    ONE_COLUMN("1 Column")
}

enum class HapticIntensity(val title: String, val scale: Float) {
    OFF("Off", 0f),
    SUBTLE("Subtle", 0.5f),
    MEDIUM("Medium", 1.0f),
    STRONG("Strong", 1.5f)
}

enum class RapidHoldSpeed(val title: String, val initialDelayMs: Long, val stepDelayMs: Long) {
    NORMAL("Normal", 160L, 80L),
    FAST("Fast", 120L, 40L),
    HYPER("Hyper", 60L, 20L)
}

enum class CounterSortOrder(val title: String) {
    LATEST_CHANGE("Latest Change"),
    CREATION_DATE("Creation Date"),
    NAME("Name (A-Z)"),
    COUNT("Highest Count")
}

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isDynamicColor: Boolean = true,
    val cornerRadiusDp: Float = 24f,
    val gridLayout: GridLayoutMode = GridLayoutMode.TWO_COLUMNS,
    val isLeftHanded: Boolean = false,
    val volumeKeysEnabled: Boolean = true,
    val keepScreenAwake: Boolean = false,
    val swipeGesturesEnabled: Boolean = true,
    val hapticIntensity: HapticIntensity = HapticIntensity.MEDIUM,
    val milestoneCelebrationEnabled: Boolean = true,
    val rapidHoldSpeed: RapidHoldSpeed = RapidHoldSpeed.FAST,
    val isPillButtons: Boolean = false,
    val sortOrder: CounterSortOrder = CounterSortOrder.LATEST_CHANGE
)
