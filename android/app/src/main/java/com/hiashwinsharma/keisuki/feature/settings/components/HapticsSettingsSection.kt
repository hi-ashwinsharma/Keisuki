package com.hiashwinsharma.keisuki.feature.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.data.preferences.HapticIntensity
import com.hiashwinsharma.keisuki.data.preferences.RapidHoldSpeed

@Composable
fun HapticsSettingsSection(
    hapticIntensity: HapticIntensity,
    milestoneCelebrationEnabled: Boolean,
    rapidHoldSpeed: RapidHoldSpeed,
    cornerRadius: Dp,
    onSetHapticIntensity: (HapticIntensity) -> Unit,
    onSetMilestoneCelebrationEnabled: (Boolean) -> Unit,
    onSetRapidHoldSpeed: (RapidHoldSpeed) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()

    Column(modifier = modifier.fillMaxWidth()) {
        SettingsSectionHeader(title = "Haptics & Acceleration", icon = Icons.Default.Vibration)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(cornerRadius),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Haptic Feedback Intensity",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(HapticIntensity.entries) { intensity ->
                            val isSelected = hapticIntensity == intensity
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    onSetHapticIntensity(intensity)
                                    if (intensity != HapticIntensity.OFF) haptics.pop()
                                },
                                label = { Text(intensity.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                shape = RoundedCornerShape(cornerRadius * 0.6f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                SettingsToggleRow(
                    title = "Milestone Celebrations",
                    subtitle = "Special crescendo vibration when hitting 10, 50, 100",
                    icon = Icons.Default.Vibration,
                    checked = milestoneCelebrationEnabled,
                    onCheckedChange = {
                        haptics.tick()
                        onSetMilestoneCelebrationEnabled(it)
                    }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Rapid-Hold Acceleration Speed",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(RapidHoldSpeed.entries) { speed ->
                            val isSelected = rapidHoldSpeed == speed
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    haptics.pop()
                                    onSetRapidHoldSpeed(speed)
                                },
                                label = { Text(speed.title, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                shape = RoundedCornerShape(cornerRadius * 0.6f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
