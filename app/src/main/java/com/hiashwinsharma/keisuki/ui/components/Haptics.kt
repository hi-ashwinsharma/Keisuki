package com.hiashwinsharma.keisuki.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.hiashwinsharma.keisuki.data.preferences.HapticIntensity

val LocalHapticIntensity = compositionLocalOf { HapticIntensity.MEDIUM }

class ExpressiveHaptics(
    private val view: View,
    private val context: Context,
    private val intensity: HapticIntensity = HapticIntensity.MEDIUM
) {
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    fun tick() {
        if (intensity == HapticIntensity.OFF) return
        val durationMs = when (intensity) {
            HapticIntensity.SUBTLE -> 6L
            HapticIntensity.MEDIUM -> 12L
            HapticIntensity.STRONG -> 22L
            HapticIntensity.OFF -> return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_TICK)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator?.hasVibrator() == true) {
            vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        }
    }

    fun pop() {
        if (intensity == HapticIntensity.OFF) return
        val durationMs = when (intensity) {
            HapticIntensity.SUBTLE -> 14L
            HapticIntensity.MEDIUM -> 24L
            HapticIntensity.STRONG -> 40L
            HapticIntensity.OFF -> return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.TOGGLE_ON)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator?.hasVibrator() == true) {
            vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    fun heavy() {
        if (intensity == HapticIntensity.OFF) return
        val durationMs = when (intensity) {
            HapticIntensity.SUBTLE -> 20L
            HapticIntensity.MEDIUM -> 45L
            HapticIntensity.STRONG -> 75L
            HapticIntensity.OFF -> return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator?.hasVibrator() == true) {
            vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    fun rapidTick() {
        if (intensity == HapticIntensity.OFF) return
        val durationMs = when (intensity) {
            HapticIntensity.SUBTLE -> 4L
            HapticIntensity.MEDIUM -> 8L
            HapticIntensity.STRONG -> 16L
            HapticIntensity.OFF -> return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator?.hasVibrator() == true) {
            vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        }
    }

    fun milestone() {
        if (intensity == HapticIntensity.OFF) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator?.hasVibrator() == true) {
            val timings = longArrayOf(0, 30, 60, 45, 80, 70)
            val amplitudes = when (intensity) {
                HapticIntensity.SUBTLE -> intArrayOf(0, 50, 0, 100, 0, 140)
                HapticIntensity.MEDIUM -> intArrayOf(0, 100, 0, 180, 0, 255)
                HapticIntensity.STRONG -> intArrayOf(0, 160, 0, 220, 0, 255)
                HapticIntensity.OFF -> return
            }
            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
        }
    }
}

@Composable
fun rememberExpressiveHaptics(): ExpressiveHaptics {
    val view = LocalView.current
    val context = LocalContext.current
    val intensity = LocalHapticIntensity.current
    return remember(view, context, intensity) {
        ExpressiveHaptics(view, context, intensity)
    }
}
