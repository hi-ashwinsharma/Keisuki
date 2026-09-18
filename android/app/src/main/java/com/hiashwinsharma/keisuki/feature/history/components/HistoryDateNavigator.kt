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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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

@OptIn(ExperimentalMaterial3Api::class)
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
        // Range mode selector - Material 3 Segmented Button Group
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            DateRangeMode.entries.forEachIndexed { index, mode ->
                val isSelected = selectedMode == mode
                SegmentedButton(
                    selected = isSelected,
                    onClick = {
                        haptics.pop()
                        onSelectMode(mode)
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = DateRangeMode.entries.size,
                        baseShape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = primaryThemeColor,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                        inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    label = {
                        Text(
                            text = mode.label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
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
