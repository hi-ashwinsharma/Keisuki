package com.hiashwinsharma.keisuki.feature.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.calculateExpressiveShapes
import com.hiashwinsharma.keisuki.feature.history.CounterEventDisplay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun LazyListScope.historyActivityLogSection(
    events: List<CounterEventDisplay>,
    counterTitle: String,
    cornerRadius: Dp
) {
    item {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Activity Log (${events.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            if (events.isEmpty()) {
                Card(
                    shape = calculateExpressiveShapes(cornerRadius).medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No events logged for $counterTitle in this period.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    val totalEvents = events.size
                    events.forEachIndexed { index, event ->
                        NotificationStackLogRow(
                            event = event,
                            shape = calculateGroupedNotificationShape(index, totalEvents, cornerRadius),
                            cornerRadius = cornerRadius
                        )
                    }
                }
            }
        }
    }
}

fun calculateGroupedNotificationShape(index: Int, total: Int, radius: Dp): RoundedCornerShape {
    val r = (radius.value * 0.75f).coerceAtLeast(8f).dp
    val sm = (radius.value * 0.15f).coerceIn(2f, 4f).dp
    return when {
        total <= 1 -> RoundedCornerShape(r)
        index == 0 -> RoundedCornerShape(topStart = r, topEnd = r, bottomStart = sm, bottomEnd = sm)
        index == total - 1 -> RoundedCornerShape(topStart = sm, topEnd = sm, bottomStart = r, bottomEnd = r)
        else -> RoundedCornerShape(sm)
    }
}

@Composable
fun NotificationStackLogRow(
    event: CounterEventDisplay,
    shape: RoundedCornerShape,
    cornerRadius: Dp,
    modifier: Modifier = Modifier
) {
    val timeFormat = remember { SimpleDateFormat("hh:mm:ss a", Locale.getDefault()) }
    val timeStr = remember(event.timestamp) { timeFormat.format(Date(event.timestamp)) }

    val isPositive = event.delta > 0
    val isNegative = event.delta < 0
    val deltaColor = when {
        isPositive -> MaterialTheme.colorScheme.primary
        isNegative -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.tertiary
    }
    val deltaText = when {
        event.eventType == "RESET" -> "Reset"
        isPositive -> "+${event.delta}"
        isNegative -> "${event.delta}"
        event.eventType == "CREATE" -> "Created"
        else -> "${event.delta}"
    }

    Surface(
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. LEFT: Bold, colored count delta text (no chip wrapper)
            Text(
                text = deltaText,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = deltaColor
            )

            // 2. MID: New Total Value
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${event.count}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // 3. RIGHT: Datetime
            Text(
                text = timeStr,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
