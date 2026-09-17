package com.hiashwinsharma.keisuki.feature.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.calculateExpressiveShapes
import com.hiashwinsharma.keisuki.core.designsystem.rememberExpressiveHaptics
import com.hiashwinsharma.keisuki.feature.history.DateRangeMode

@Composable
fun HistoryDateNavigator(
    selectedMode: DateRangeMode,
    dateDisplayLabel: String,
    primaryThemeColor: Color,
    cornerRadius: Dp,
    onSelectMode: (DateRangeMode) -> Unit,
    onPreviousPeriod: () -> Unit,
    onNextPeriod: () -> Unit,
    onDatePickerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    val shapes = calculateExpressiveShapes(cornerRadius)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Range mode selector chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DateRangeMode.entries.forEach { mode ->
                val isSelected = selectedMode == mode
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        haptics.pop()
                        onSelectMode(mode)
                    },
                    label = {
                        Text(
                            text = mode.label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    shape = shapes.small,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = primaryThemeColor,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        // Date switcher (< Date >)
        Surface(
            shape = shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    haptics.tick()
                    onPreviousPeriod()
                }) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous Period",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = dateDisplayLabel,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clip(shapes.extraSmall)
                        .clickable {
                            haptics.tick()
                            onDatePickerClick()
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                IconButton(onClick = {
                    haptics.tick()
                    onNextPeriod()
                }) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next Period",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
