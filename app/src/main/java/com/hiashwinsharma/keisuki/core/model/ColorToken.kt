package com.hiashwinsharma.keisuki.core.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance

enum class ColorRole {
    DYNAMIC_PRIMARY,
    DYNAMIC_TERTIARY,
    DYNAMIC_ACCENT,
    DYNAMIC_SECONDARY,
    CUSTOM
}

enum class ColorToken(
    val id: String,
    val displayName: String,
    val role: ColorRole = ColorRole.CUSTOM,
    val customPrimary: Color = Color.Unspecified,
    val lightContainerColor: Color = Color.Unspecified,
    val lightOnContainerColor: Color = Color.Unspecified,
    val darkContainerColor: Color = Color.Unspecified,
    val darkOnContainerColor: Color = Color.Unspecified,
    val customOnPrimary: Color = Color.White
) {
    // 4 Distinct Dynamic Wallpaper Roles (Guaranteed No Repeated Consecutive Shades)
    DYNAMIC_PRIMARY(
        id = "dynamic_primary",
        displayName = "Wallpaper Primary",
        role = ColorRole.DYNAMIC_PRIMARY
    ),
    DYNAMIC_TERTIARY(
        id = "dynamic_tertiary",
        displayName = "Wallpaper Tertiary",
        role = ColorRole.DYNAMIC_TERTIARY
    ),
    DYNAMIC_ACCENT(
        id = "dynamic_accent",
        displayName = "Wallpaper Accent",
        role = ColorRole.DYNAMIC_ACCENT
    ),
    DYNAMIC_SECONDARY(
        id = "dynamic_secondary",
        displayName = "Wallpaper Secondary",
        role = ColorRole.DYNAMIC_SECONDARY
    ),

    // 12 Fixed High-Chroma Expressive Colors with Vibrant Dark Containers & High Contrast
    ELECTRIC_ROSE(
        id = "electric_rose",
        displayName = "Electric Rose",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFFFF1774),
        lightContainerColor = Color(0xFFFFD8E4),
        lightOnContainerColor = Color(0xFF3B0014),
        darkContainerColor = Color(0xFF4A1028),
        darkOnContainerColor = Color(0xFFFFD8E4),
        customOnPrimary = Color.White
    ),
    NEON_JADE(
        id = "neon_jade",
        displayName = "Neon Jade",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFF00C853),
        lightContainerColor = Color(0xFFC8FCD9),
        lightOnContainerColor = Color(0xFF003914),
        darkContainerColor = Color(0xFF003D1A),
        darkOnContainerColor = Color(0xFFC8FCD9),
        customOnPrimary = Color(0xFF003914)
    ),
    CYBER_CORAL(
        id = "cyber_coral",
        displayName = "Cyber Coral",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFFFF5252),
        lightContainerColor = Color(0xFFFFDAD6),
        lightOnContainerColor = Color(0xFF410002),
        darkContainerColor = Color(0xFF4A1414),
        darkOnContainerColor = Color(0xFFFFDAD6),
        customOnPrimary = Color.White
    ),
    HYPER_CYAN(
        id = "hyper_cyan",
        displayName = "Hyper Cyan",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFF00B0FF),
        lightContainerColor = Color(0xFFC6E7FF),
        lightOnContainerColor = Color(0xFF003258),
        darkContainerColor = Color(0xFF003554),
        darkOnContainerColor = Color(0xFFC6E7FF),
        customOnPrimary = Color(0xFF003258)
    ),
    SOLAR_AMBER(
        id = "solar_amber",
        displayName = "Solar Amber",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFFFF9100),
        lightContainerColor = Color(0xFFFFDDB3),
        lightOnContainerColor = Color(0xFF2A1800),
        darkContainerColor = Color(0xFF4A2600),
        darkOnContainerColor = Color(0xFFFFDDB3),
        customOnPrimary = Color(0xFF2A1800)
    ),
    DEEP_VIOLET(
        id = "deep_violet",
        displayName = "Deep Violet",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFF7C4DFF),
        lightContainerColor = Color(0xFFEADDFF),
        lightOnContainerColor = Color(0xFF21005D),
        darkContainerColor = Color(0xFF2E1560),
        darkOnContainerColor = Color(0xFFEADDFF),
        customOnPrimary = Color.White
    ),
    ELECTRIC_LIME(
        id = "electric_lime",
        displayName = "Electric Lime",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFF64DD17),
        lightContainerColor = Color(0xFFE2F9C2),
        lightOnContainerColor = Color(0xFF1E3500),
        darkContainerColor = Color(0xFF233D00),
        darkOnContainerColor = Color(0xFFE2F9C2),
        customOnPrimary = Color(0xFF1E3500)
    ),
    ROYAL_INDIGO(
        id = "royal_indigo",
        displayName = "Royal Indigo",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFF536DFE),
        lightContainerColor = Color(0xFFDEE0FF),
        lightOnContainerColor = Color(0xFF00115A),
        darkContainerColor = Color(0xFF192468),
        darkOnContainerColor = Color(0xFFDEE0FF),
        customOnPrimary = Color.White
    ),
    SUNSET_ORANGE(
        id = "sunset_orange",
        displayName = "Sunset Orange",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFFFF6D00),
        lightContainerColor = Color(0xFFFFE0B2),
        lightOnContainerColor = Color(0xFF4E2600),
        darkContainerColor = Color(0xFF4C2100),
        darkOnContainerColor = Color(0xFFFFE0B2),
        customOnPrimary = Color.White
    ),
    MAGENTA_PUNCH(
        id = "magenta_punch",
        displayName = "Magenta Punch",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFFE040FB),
        lightContainerColor = Color(0xFFF8BBD0),
        lightOnContainerColor = Color(0xFF4A0042),
        darkContainerColor = Color(0xFF4A0846),
        darkOnContainerColor = Color(0xFFF8BBD0),
        customOnPrimary = Color.White
    ),
    OCEAN_TEAL(
        id = "ocean_teal",
        displayName = "Ocean Teal",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFF00BFA5),
        lightContainerColor = Color(0xFFB2DFDB),
        lightOnContainerColor = Color(0xFF004D40),
        darkContainerColor = Color(0xFF003C34),
        darkOnContainerColor = Color(0xFFB2DFDB),
        customOnPrimary = Color(0xFF00382E)
    ),
    CRIMSON_FLAME(
        id = "crimson_flame",
        displayName = "Crimson Flame",
        role = ColorRole.CUSTOM,
        customPrimary = Color(0xFFFF1744),
        lightContainerColor = Color(0xFFFFCDD2),
        lightOnContainerColor = Color(0xFF4A0004),
        darkContainerColor = Color(0xFF4D000C),
        darkOnContainerColor = Color(0xFFFFCDD2),
        customOnPrimary = Color.White
    );

    val isCustomColor: Boolean get() = role == ColorRole.CUSTOM
    val isDynamic: Boolean get() = role != ColorRole.CUSTOM

    val primaryColor: Color
        @Composable
        @ReadOnlyComposable
        get() = when (role) {
            ColorRole.CUSTOM -> customPrimary
            ColorRole.DYNAMIC_PRIMARY -> MaterialTheme.colorScheme.primary
            ColorRole.DYNAMIC_TERTIARY -> MaterialTheme.colorScheme.tertiary
            ColorRole.DYNAMIC_ACCENT -> MaterialTheme.colorScheme.error
            ColorRole.DYNAMIC_SECONDARY -> MaterialTheme.colorScheme.secondary
        }

    val containerColor: Color
        @Composable
        @ReadOnlyComposable
        get() {
            val bg = MaterialTheme.colorScheme.background
            val isDark = bg.luminance() < 0.5f
            val isAmoled = bg == Color.Black || bg.luminance() < 0.02f

            val rawColor = when (role) {
                ColorRole.DYNAMIC_PRIMARY -> MaterialTheme.colorScheme.primaryContainer
                ColorRole.DYNAMIC_TERTIARY -> MaterialTheme.colorScheme.tertiaryContainer
                ColorRole.DYNAMIC_ACCENT -> MaterialTheme.colorScheme.errorContainer
                ColorRole.DYNAMIC_SECONDARY -> MaterialTheme.colorScheme.secondaryContainer
                ColorRole.CUSTOM -> if (isDark) darkContainerColor else lightContainerColor
            }

            return if (isDark) {
                val baseDark = if (isAmoled) Color(0xFF141414) else Color(0xFF222026)
                if (rawColor.luminance() > 0.15f) {
                    lerp(baseDark, rawColor, 0.35f)
                } else {
                    rawColor
                }
            } else {
                rawColor
            }
        }

    val onContainerColor: Color
        @Composable
        @ReadOnlyComposable
        get() {
            val bg = MaterialTheme.colorScheme.background
            val isDark = bg.luminance() < 0.5f

            val rawColor = when (role) {
                ColorRole.DYNAMIC_PRIMARY -> MaterialTheme.colorScheme.onPrimaryContainer
                ColorRole.DYNAMIC_TERTIARY -> MaterialTheme.colorScheme.onTertiaryContainer
                ColorRole.DYNAMIC_ACCENT -> MaterialTheme.colorScheme.onErrorContainer
                ColorRole.DYNAMIC_SECONDARY -> MaterialTheme.colorScheme.onSecondaryContainer
                ColorRole.CUSTOM -> if (isDark) darkOnContainerColor else lightOnContainerColor
            }

            return if (isDark) {
                if (rawColor.luminance() < 0.82f) {
                    lerp(Color.White, rawColor, 0.15f)
                } else {
                    rawColor
                }
            } else {
                if (rawColor.luminance() > 0.25f) {
                    lerp(Color.Black, rawColor, 0.25f)
                } else {
                    rawColor
                }
            }
        }

    val onPrimaryColor: Color
        @Composable
        @ReadOnlyComposable
        get() = when (role) {
            ColorRole.CUSTOM -> customOnPrimary
            ColorRole.DYNAMIC_PRIMARY -> MaterialTheme.colorScheme.onPrimary
            ColorRole.DYNAMIC_TERTIARY -> MaterialTheme.colorScheme.onTertiary
            ColorRole.DYNAMIC_ACCENT -> MaterialTheme.colorScheme.onError
            ColorRole.DYNAMIC_SECONDARY -> MaterialTheme.colorScheme.onSecondary
        }

    fun nextColor(): ColorToken {
        val dynamicTokens = entries.filter { it.isDynamic }
        val currentIndex = dynamicTokens.indexOf(this)
        return if (currentIndex >= 0 && currentIndex + 1 < dynamicTokens.size) {
            dynamicTokens[currentIndex + 1]
        } else {
            dynamicTokens.first()
        }
    }

    companion object {
        fun fromId(id: String): ColorToken {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) }
                ?: when (id.lowercase()) {
                    "default" -> DYNAMIC_PRIMARY
                    "dynamic_inverse" -> DYNAMIC_ACCENT
                    "dynamic_neutral" -> DYNAMIC_SECONDARY
                    else -> DYNAMIC_PRIMARY
                }
        }
    }
}
