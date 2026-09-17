package com.hiashwinsharma.keisuki.feature.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics

@Composable
fun ErgonomicsSettingsSection(
    isLeftHanded: Boolean,
    volumeKeysEnabled: Boolean,
    keepScreenAwake: Boolean,
    swipeGesturesEnabled: Boolean,
    cornerRadius: Dp,
    onSetLeftHanded: (Boolean) -> Unit,
    onSetVolumeKeysEnabled: (Boolean) -> Unit,
    onSetKeepScreenAwake: (Boolean) -> Unit,
    onSetSwipeGesturesEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()

    Column(modifier = modifier.fillMaxWidth()) {
        SettingsSectionHeader(title = "Ergonomics & Controls", icon = Icons.Default.PanTool)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(cornerRadius),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingsToggleRow(
                    title = "Left-Handed Mode",
                    subtitle = "Flips the dock: places the large Add button on the left",
                    icon = Icons.Default.TouchApp,
                    checked = isLeftHanded,
                    onCheckedChange = {
                        haptics.tick()
                        onSetLeftHanded(it)
                    }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                SettingsToggleRow(
                    title = "Volume Button Counting",
                    subtitle = "Use physical Volume Up / Down buttons in focus mode",
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    checked = volumeKeysEnabled,
                    onCheckedChange = {
                        haptics.tick()
                        onSetVolumeKeysEnabled(it)
                    }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                SettingsToggleRow(
                    title = "Keep Screen Awake",
                    subtitle = "Prevents display from sleeping while in counter focus",
                    icon = Icons.Default.PhoneAndroid,
                    checked = keepScreenAwake,
                    onCheckedChange = {
                        haptics.tick()
                        onSetKeepScreenAwake(it)
                    }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                SettingsToggleRow(
                    title = "Full-Screen Swipe Gestures",
                    subtitle = "Swipe up anywhere to increment, swipe down to decrement",
                    icon = Icons.Default.PanTool,
                    checked = swipeGesturesEnabled,
                    onCheckedChange = {
                        haptics.tick()
                        onSetSwipeGesturesEnabled(it)
                    }
                )
            }
        }
    }
}
