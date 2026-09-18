package com.hiashwinsharma.keisuki.core.ui.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.ui.ChartPoint

@Composable
fun ChartCanvas(
    sortedPoints: List<ChartPoint>,
    effectiveStartTime: Long,
    effectiveEndTime: Long,
    primaryColor: Color,
    emptyStateMessage: String,
    scrubberX: Float?,
    scrubbedPoint: ChartPoint?,
    activeScrubberAlpha: Float,
    onScrubPositionChange: (Float, Float) -> Unit,
    onScrubEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val outlineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        if (sortedPoints.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emptyStateMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(sortedPoints, effectiveStartTime, effectiveEndTime) {
                    detectTapGestures(
                        onPress = { offset ->
                            onScrubPositionChange(offset.x, size.width.toFloat())
                            tryAwaitRelease()
                            onScrubEnd()
                        }
                    )
                }
                .pointerInput(sortedPoints, effectiveStartTime, effectiveEndTime) {
                    detectDragGestures(
                        onDragStart = { offset -> onScrubPositionChange(offset.x, size.width.toFloat()) },
                        onDrag = { change, _ ->
                            change.consume()
                            onScrubPositionChange(change.position.x, size.width.toFloat())
                        },
                        onDragEnd = { onScrubEnd() },
                        onDragCancel = { onScrubEnd() }
                    )
                }
        ) {
            val bottomPadding = 32.dp.toPx()
            val topPadding = 16.dp.toPx()
            val graphHeight = size.height - bottomPadding - topPadding

            drawChartGrid(
                width = size.width,
                graphHeight = graphHeight,
                topPadding = topPadding,
                outlineColor = outlineColor
            )

            drawTimeAxisLabels(
                textMeasurer = textMeasurer,
                width = size.width,
                height = size.height,
                effectiveStartTime = effectiveStartTime,
                effectiveEndTime = effectiveEndTime,
                onSurfaceVariant = onSurfaceVariant
            )

            drawChartData(
                sortedPoints = sortedPoints,
                effectiveStartTime = effectiveStartTime,
                effectiveEndTime = effectiveEndTime,
                width = size.width,
                topPadding = topPadding,
                graphHeight = graphHeight,
                primaryColor = primaryColor,
                scrubberX = scrubberX,
                scrubbedPoint = scrubbedPoint,
                activeScrubberAlpha = activeScrubberAlpha
            )
        }
    }
}
