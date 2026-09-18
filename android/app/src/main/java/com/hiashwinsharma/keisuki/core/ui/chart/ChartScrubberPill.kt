package com.hiashwinsharma.keisuki.core.ui.chart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.LocalAppCornerRadius
import com.hiashwinsharma.keisuki.core.designsystem.calculateExpressiveShapes
import com.hiashwinsharma.keisuki.core.ui.ChartPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChartScrubberHeader(
    scrubbedPoint: ChartPoint?,
    latestPoint: ChartPoint?,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        AnimatedVisibility(
            visible = scrubbedPoint != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            scrubbedPoint?.let { sp ->
                val appRadius = LocalAppCornerRadius.current
                Surface(
                    shape = calculateExpressiveShapes(appRadius).small,
                    color = primaryColor.copy(alpha = 0.16f),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val timeFormat = remember { SimpleDateFormat("hh:mm:ss a, MMM d", Locale.getDefault()) }
                        Text(
                            text = timeFormat.format(Date(sp.timestamp)),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Count: ${sp.count}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = primaryColor
                        )
                    }
                }
            }
        }

        if (scrubbedPoint == null && latestPoint != null) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current: ${latestPoint.count}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "• Touch to scrub timeline",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
