package com.hiashwinsharma.keisuki.feature.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.hiashwinsharma.keisuki.core.ui.BatteryStyleChart
import com.hiashwinsharma.keisuki.core.ui.ChartPoint

@Composable
fun HistoryChartCard(
    counterTitle: String,
    chartPoints: List<ChartPoint>,
    startTime: Long,
    endTime: Long,
    primaryThemeColor: Color,
    isAutoZoom: Boolean,
    canZoom: Boolean,
    cornerRadius: Dp,
    onToggleAutoZoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberExpressiveHaptics()
    val shapes = calculateExpressiveShapes(cornerRadius)

    Card(
        shape = shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$counterTitle Timeline",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (canZoom) {
                        Surface(
                            shape = shapes.extraSmall,
                            color = if (isAutoZoom) primaryThemeColor.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceContainerHigh,
                            modifier = Modifier
                                .clip(shapes.extraSmall)
                                .clickable {
                                    haptics.pop()
                                    onToggleAutoZoom()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (isAutoZoom) Icons.Default.ZoomIn else Icons.Default.ZoomOut,
                                    contentDescription = null,
                                    tint = if (isAutoZoom) primaryThemeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (isAutoZoom) "Active Session" else "Full Day",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (isAutoZoom) primaryThemeColor else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Surface(
                        shape = shapes.extraSmall,
                        color = primaryThemeColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${chartPoints.size} pts",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = primaryThemeColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            BatteryStyleChart(
                points = chartPoints,
                startTime = startTime,
                endTime = endTime,
                primaryColor = primaryThemeColor,
                isAutoZoom = isAutoZoom,
                emptyStateMessage = "No activity for $counterTitle in this period"
            )
        }
    }
}
