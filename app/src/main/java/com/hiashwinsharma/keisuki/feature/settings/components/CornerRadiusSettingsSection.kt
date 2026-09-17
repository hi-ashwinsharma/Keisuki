package com.hiashwinsharma.keisuki.feature.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RoundedCorner
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics

@Composable
fun CornerRadiusSettingsSection(
    cornerRadiusDp: Float,
    isPillButtons: Boolean,
    cornerRadius: Dp,
    onSetCornerRadius: (Float) -> Unit,
    onSetPillButtons: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()

    Column(modifier = modifier.fillMaxWidth()) {
        SettingsSectionHeader(title = "Corner Geometry & Shapes", icon = Icons.Default.RoundedCorner)
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
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Corner Curvature",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Current: ${cornerRadiusDp.toInt()} dp",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    if (cornerRadiusDp > 0f) {
                                        haptics.tick()
                                        onSetCornerRadius((cornerRadiusDp - 2f).coerceAtLeast(0f))
                                    }
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease radius", modifier = Modifier.size(18.dp))
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                modifier = Modifier.padding(horizontal = 2.dp)
                            ) {
                                Text(
                                    text = "${cornerRadiusDp.toInt()} dp",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (cornerRadiusDp < 36f) {
                                        haptics.tick()
                                        onSetCornerRadius((cornerRadiusDp + 2f).coerceAtMost(36f))
                                    }
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase radius", modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    val presets = listOf(
                        "Sharp" to 4f,
                        "Compact" to 12f,
                        "Medium" to 20f,
                        "Expressive" to 28f,
                        "Full" to 34f
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(presets) { (name, r) ->
                            val isSelected = cornerRadiusDp == r
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    haptics.pop()
                                    onSetCornerRadius(r)
                                },
                                label = { Text(name, fontSize = 12.sp) },
                                shape = RoundedCornerShape(r.dp * 0.5f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                SettingsToggleRow(
                    title = "Pill Buttons",
                    subtitle = "Make the focus screen Add and Subtract buttons pure pills",
                    icon = Icons.Default.Category,
                    checked = isPillButtons,
                    onCheckedChange = {
                        haptics.tick()
                        onSetPillButtons(it)
                    }
                )
            }
        }
    }
}
