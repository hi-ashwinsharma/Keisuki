package com.hiashwinsharma.keisuki.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import com.hiashwinsharma.keisuki.data.preferences.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB1C8),
    onPrimary = Color(0xFF5E112B),
    primaryContainer = Color(0xFF7B2943),
    onPrimaryContainer = Color(0xFFFFD8E4),
    secondary = Color(0xFF80D4FF),
    onSecondary = Color(0xFF00344D),
    secondaryContainer = Color(0xFF004C6E),
    onSecondaryContainer = Color(0xFFC6E7FF),
    tertiary = Color(0xFF6CDB8E),
    onTertiary = Color(0xFF003915),
    tertiaryContainer = Color(0xFF005322),
    onTertiaryContainer = Color(0xFFC8FCD9),
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest
)

private val AmoledColorScheme = darkColorScheme(
    primary = Color(0xFFFFB1C8),
    onPrimary = Color(0xFF5E112B),
    primaryContainer = Color(0xFF4A1022),
    onPrimaryContainer = Color(0xFFFFD8E4),
    secondary = Color(0xFF80D4FF),
    onSecondary = Color(0xFF00344D),
    secondaryContainer = Color(0xFF003248),
    onSecondaryContainer = Color(0xFFC6E7FF),
    tertiary = Color(0xFF6CDB8E),
    onTertiary = Color(0xFF003915),
    tertiaryContainer = Color(0xFF003B17),
    onTertiaryContainer = Color(0xFFC8FCD9),
    background = Color(0xFF000000),
    onBackground = Color(0xFFF5F5F5),
    surface = Color(0xFF000000),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF141414),
    onSurfaceVariant = Color(0xFFCCCCCC),
    surfaceContainer = Color(0xFF0A0A0A),
    surfaceContainerHigh = Color(0xFF141414),
    surfaceContainerHighest = Color(0xFF1F1F1F)
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricRose,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = ElectricRoseContainer,
    onPrimaryContainer = OnElectricRoseContainer,
    secondary = HyperCyan,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = HyperCyanContainer,
    onSecondaryContainer = OnHyperCyanContainer,
    tertiary = NeonJade,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = NeonJadeContainer,
    onTertiaryContainer = OnNeonJadeContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest
)

val LocalAppCornerRadius = compositionLocalOf { 24.dp }

fun calculateExpressiveShapes(radius: Dp): Shapes {
    val r = radius.value
    return Shapes(
        extraSmall = RoundedCornerShape((r * 0.35f).coerceAtLeast(0f).dp),
        small = RoundedCornerShape((r * 0.55f).coerceAtLeast(0f).dp),
        medium = RoundedCornerShape((r * 0.85f).coerceAtLeast(0f).dp),
        large = RoundedCornerShape(r.coerceAtLeast(0f).dp),
        extraLarge = RoundedCornerShape((r * 1.3f).coerceAtLeast(0f).dp)
    )
}

val ExpressiveShapes = calculateExpressiveShapes(24.dp)

@Composable
fun KeisukiTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = true,
    cornerRadius: Dp = 24.dp,
    content: @Composable () -> Unit
) {
    val systemInDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> systemInDark
        ThemeMode.DARK, ThemeMode.AMOLED -> true
        ThemeMode.LIGHT -> false
    }

    val isAmoled = themeMode == ThemeMode.AMOLED

    val colorScheme = when {
        isAmoled -> AmoledColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }

    val shapes = calculateExpressiveShapes(cornerRadius)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            window.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(colorScheme.background.toArgb()))
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isDark
                isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    CompositionLocalProvider(LocalAppCornerRadius provides cornerRadius) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = ExpressiveTypography,
            shapes = shapes,
            content = content
        )
    }
}