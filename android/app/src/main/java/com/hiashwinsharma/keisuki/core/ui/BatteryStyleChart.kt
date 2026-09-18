package com.hiashwinsharma.keisuki.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.ui.chart.ChartCanvas
import com.hiashwinsharma.keisuki.core.ui.chart.ChartMathUtils
import com.hiashwinsharma.keisuki.core.ui.chart.ChartScrubberHeader

data class ChartPoint(
    val timestamp: Long,
    val count: Long,
    val label: String = ""
)

@Composable
fun BatteryStyleChart(
    points: List<ChartPoint>,
    startTime: Long,
    endTime: Long,
    primaryColor: Color,
    modifier: Modifier = Modifier,
    isAutoZoom: Boolean = true,
    emptyStateMessage: String = "No activity in this period"
) {
    val sortedPoints = remember(points) { points.sortedBy { it.timestamp } }
    val (effectiveStartTime, effectiveEndTime) = remember(sortedPoints, startTime, endTime, isAutoZoom) {
        ChartMathUtils.calculateEffectiveFraming(sortedPoints, startTime, endTime, isAutoZoom)
    }

    var scrubberX by remember { mutableStateOf<Float?>(null) }
    var scrubbedPoint by remember { mutableStateOf<ChartPoint?>(null) }

    val activeScrubberAlpha by animateFloatAsState(
        targetValue = if (scrubberX != null) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "ScrubberAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        ChartScrubberHeader(
            scrubbedPoint = scrubbedPoint,
            latestPoint = sortedPoints.lastOrNull(),
            primaryColor = primaryColor
        )

        ChartCanvas(
            sortedPoints = sortedPoints,
            effectiveStartTime = effectiveStartTime,
            effectiveEndTime = effectiveEndTime,
            primaryColor = primaryColor,
            emptyStateMessage = emptyStateMessage,
            scrubberX = scrubberX,
            scrubbedPoint = scrubbedPoint,
            activeScrubberAlpha = activeScrubberAlpha,
            onScrubPositionChange = { rawX, canvasWidth ->
                scrubberX = rawX
                scrubbedPoint = ChartMathUtils.findClosestPoint(
                    rawX, canvasWidth, sortedPoints, effectiveStartTime, effectiveEndTime
                )
            },
            onScrubEnd = {
                scrubberX = null
                scrubbedPoint = null
            }
        )
    }
}
