package com.hiashwinsharma.keisuki.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val textMeasurer = rememberTextMeasurer()
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val outlineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)

    // Calculate effective framing (Smart Auto-Zoom when points are clustered in a short session)
    val sortedPoints = remember(points) { points.sortedBy { it.timestamp } }
    val (effectiveStartTime, effectiveEndTime) = remember(sortedPoints, startTime, endTime, isAutoZoom) {
        if (isAutoZoom && sortedPoints.size >= 2) {
            val minPointTime = sortedPoints.first().timestamp
            val maxPointTime = sortedPoints.last().timestamp
            val activeSpan = (maxPointTime - minPointTime).coerceAtLeast(1000L)
            val windowSpan = endTime - startTime

            // If activity is concentrated within less than 70% of the window
            if (activeSpan < windowSpan * 0.7f) {
                val paddingMs = (activeSpan * 0.20f).toLong().coerceIn(30_000L, 30 * 60_000L)
                val zStart = (minPointTime - paddingMs).coerceAtLeast(startTime)
                val zEnd = (maxPointTime + paddingMs).coerceAtMost(endTime)
                Pair(zStart, zEnd)
            } else {
                Pair(startTime, endTime)
            }
        } else {
            Pair(startTime, endTime)
        }
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
        // Scrubber Info Pill Display at top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = scrubbedPoint != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                scrubbedPoint?.let { sp ->
                    val appRadius = com.hiashwinsharma.keisuki.core.designsystem.LocalAppCornerRadius.current
                    Surface(
                        shape = com.hiashwinsharma.keisuki.core.designsystem.calculateExpressiveShapes(appRadius).small,
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

            if (scrubbedPoint == null && sortedPoints.isNotEmpty()) {
                val latest = sortedPoints.last()
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current: ${latest.count}",
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

        // Chart Canvas Area
        Box(
            modifier = Modifier
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
                                scrubberX = offset.x
                                scrubbedPoint = findClosestPoint(offset.x, size.width.toFloat(), sortedPoints, effectiveStartTime, effectiveEndTime)
                                tryAwaitRelease()
                                scrubberX = null
                                scrubbedPoint = null
                            }
                        )
                    }
                    .pointerInput(sortedPoints, effectiveStartTime, effectiveEndTime) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                scrubberX = offset.x
                                scrubbedPoint = findClosestPoint(offset.x, size.width.toFloat(), sortedPoints, effectiveStartTime, effectiveEndTime)
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                scrubberX = change.position.x
                                scrubbedPoint = findClosestPoint(change.position.x, size.width.toFloat(), sortedPoints, effectiveStartTime, effectiveEndTime)
                            },
                            onDragEnd = {
                                scrubberX = null
                                scrubbedPoint = null
                            },
                            onDragCancel = {
                                scrubberX = null
                                scrubbedPoint = null
                            }
                        )
                    }
            ) {
                val width = size.width
                val height = size.height
                val bottomPadding = 32.dp.toPx()
                val topPadding = 16.dp.toPx()
                val graphHeight = height - bottomPadding - topPadding

                // Grid lines
                val gridLineCount = 3
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)

                for (i in 0..gridLineCount) {
                    val y = topPadding + (graphHeight * i / gridLineCount)
                    drawLine(
                        color = outlineColor,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = dashEffect
                    )
                }

                // Time axis markers (Bottom) with Adaptive Formatter
                val totalTimeSpan = (effectiveEndTime - effectiveStartTime).coerceAtLeast(1L)
                val timeLabelCount = 4

                val timeFormatPattern = when {
                    totalTimeSpan <= 10 * 60 * 1000L -> "h:mm:ss a"   // <= 10 mins: seconds
                    totalTimeSpan <= 6 * 3600 * 1000L -> "h:mm a"     // <= 6 hours: minute precision
                    totalTimeSpan <= 48 * 3600 * 1000L -> "h a"       // <= 2 days: hour
                    else -> "MMM d"                                   // Multiday / week / month
                }
                val timeFormat = SimpleDateFormat(timeFormatPattern, Locale.getDefault())

                for (i in 0..timeLabelCount) {
                    val progress = i.toFloat() / timeLabelCount
                    val markerTime = effectiveStartTime + (totalTimeSpan * progress).toLong()
                    val x = width * progress
                    val timeStr = timeFormat.format(Date(markerTime))

                    val textLayout = textMeasurer.measure(
                        text = timeStr,
                        style = TextStyle(
                            fontSize = 9.sp,
                            color = onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    val textX = (x - textLayout.size.width / 2f).coerceIn(0f, width - textLayout.size.width)
                    drawText(
                        textLayoutResult = textLayout,
                        topLeft = Offset(textX, height - 20.dp.toPx())
                    )
                }

                if (sortedPoints.isNotEmpty()) {
                    val minVal = (sortedPoints.minOfOrNull { it.count } ?: 0L).coerceAtLeast(0L)
                    val maxVal = ((sortedPoints.maxOfOrNull { it.count } ?: 0L) + 1L).coerceAtLeast(minVal + 4L)
                    val valRange = (maxVal - minVal).toFloat().coerceAtLeast(1f)

                    val path = Path()
                    val fillPath = Path()
                    val mappedCoordinates = mutableListOf<Offset>()

                    // Filter or clamp points in visible range
                    val visiblePoints = sortedPoints.filter { it.timestamp in effectiveStartTime..effectiveEndTime }
                    val activeList = if (visiblePoints.isNotEmpty()) visiblePoints else sortedPoints

                    // Left anchor
                    val firstPt = activeList.first()
                    val firstX = ((firstPt.timestamp - effectiveStartTime).toFloat() / totalTimeSpan * width).coerceIn(0f, width)
                    val firstY = topPadding + graphHeight * (1f - (firstPt.count - minVal) / valRange)

                    if (firstX > 0f) {
                        mappedCoordinates.add(Offset(0f, firstY))
                    }

                    for (pt in activeList) {
                        val px = ((pt.timestamp - effectiveStartTime).toFloat() / totalTimeSpan * width).coerceIn(0f, width)
                        val py = topPadding + graphHeight * (1f - (pt.count - minVal) / valRange)
                        mappedCoordinates.add(Offset(px, py))
                    }

                    // Right anchor
                    val lastPt = activeList.last()
                    val lastX = ((lastPt.timestamp - effectiveStartTime).toFloat() / totalTimeSpan * width).coerceIn(0f, width)
                    val lastY = topPadding + graphHeight * (1f - (lastPt.count - minVal) / valRange)
                    if (lastX < width) {
                        mappedCoordinates.add(Offset(width, lastY))
                    }

                    if (mappedCoordinates.isNotEmpty()) {
                        path.moveTo(mappedCoordinates.first().x, mappedCoordinates.first().y)
                        fillPath.moveTo(mappedCoordinates.first().x, topPadding + graphHeight)
                        fillPath.lineTo(mappedCoordinates.first().x, mappedCoordinates.first().y)

                        for (i in 0 until mappedCoordinates.size - 1) {
                            val p1 = mappedCoordinates[i]
                            val p2 = mappedCoordinates[i + 1]

                            val midX = (p1.x + p2.x) / 2f
                            path.cubicTo(
                                midX, p1.y,
                                midX, p2.y,
                                p2.x, p2.y
                            )
                            fillPath.cubicTo(
                                midX, p1.y,
                                midX, p2.y,
                                p2.x, p2.y
                            )
                        }

                        fillPath.lineTo(mappedCoordinates.last().x, topPadding + graphHeight)
                        fillPath.close()

                        // Draw Gradient Area Fill
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = 0.38f),
                                    primaryColor.copy(alpha = 0.08f),
                                    Color.Transparent
                                ),
                                startY = topPadding,
                                endY = topPadding + graphHeight
                            )
                        )

                        // Draw Line Curve
                        drawPath(
                            path = path,
                            color = primaryColor,
                            style = Stroke(
                                width = 3.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )

                        // Draw Scrubber if active
                        scrubberX?.let { sx ->
                            val clampedX = sx.coerceIn(0f, width)
                            drawLine(
                                color = primaryColor.copy(alpha = 0.7f * activeScrubberAlpha),
                                start = Offset(clampedX, topPadding),
                                end = Offset(clampedX, topPadding + graphHeight),
                                strokeWidth = 2.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                            )

                            scrubbedPoint?.let { sp ->
                                val ptY = topPadding + graphHeight * (1f - (sp.count - minVal) / valRange)
                                val ptX = ((sp.timestamp - effectiveStartTime).toFloat() / totalTimeSpan * width).coerceIn(0f, width)

                                drawCircle(
                                    color = Color.White,
                                    radius = 6.dp.toPx(),
                                    center = Offset(ptX, ptY)
                                )
                                drawCircle(
                                    color = primaryColor,
                                    radius = 4.dp.toPx(),
                                    center = Offset(ptX, ptY)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun findClosestPoint(
    touchX: Float,
    canvasWidth: Float,
    points: List<ChartPoint>,
    startTime: Long,
    endTime: Long
): ChartPoint? {
    if (points.isEmpty() || canvasWidth <= 0f) return null
    val totalTime = (endTime - startTime).coerceAtLeast(1L)
    val touchTimestamp = startTime + ((touchX / canvasWidth).coerceIn(0f, 1f) * totalTime).toLong()

    return points.minByOrNull { Math.abs(it.timestamp - touchTimestamp) }
}
