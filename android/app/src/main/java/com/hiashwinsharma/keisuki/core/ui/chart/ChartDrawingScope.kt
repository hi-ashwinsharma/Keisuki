package com.hiashwinsharma.keisuki.core.ui.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiashwinsharma.keisuki.core.ui.ChartPoint

fun DrawScope.drawChartGrid(
    width: Float,
    graphHeight: Float,
    topPadding: Float,
    outlineColor: Color
) {
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
}

fun DrawScope.drawTimeAxisLabels(
    textMeasurer: TextMeasurer,
    width: Float,
    height: Float,
    effectiveStartTime: Long,
    effectiveEndTime: Long,
    onSurfaceVariant: Color
) {
    val totalTimeSpan = (effectiveEndTime - effectiveStartTime).coerceAtLeast(1L)
    val timeLabelCount = 4
    val timePattern = ChartMathUtils.getTimeFormatPattern(totalTimeSpan)

    for (i in 0..timeLabelCount) {
        val progress = i.toFloat() / timeLabelCount
        val markerTime = effectiveStartTime + (totalTimeSpan * progress).toLong()
        val x = width * progress
        val timeStr = ChartMathUtils.formatMarkerTime(markerTime, timePattern)

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
}

fun DrawScope.drawChartData(
    sortedPoints: List<ChartPoint>,
    effectiveStartTime: Long,
    effectiveEndTime: Long,
    width: Float,
    topPadding: Float,
    graphHeight: Float,
    primaryColor: Color,
    scrubberX: Float?,
    scrubbedPoint: ChartPoint?,
    activeScrubberAlpha: Float
) {
    if (sortedPoints.isEmpty()) return
    val totalTimeSpan = (effectiveEndTime - effectiveStartTime).coerceAtLeast(1L)
    val minVal = (sortedPoints.minOfOrNull { it.count } ?: 0L).coerceAtLeast(0L)
    val maxVal = ((sortedPoints.maxOfOrNull { it.count } ?: 0L) + 1L).coerceAtLeast(minVal + 4L)
    val valRange = (maxVal - minVal).toFloat().coerceAtLeast(1f)

    val path = Path()
    val fillPath = Path()
    val mappedCoordinates = mutableListOf<Offset>()

    val visiblePoints = sortedPoints.filter { it.timestamp in effectiveStartTime..effectiveEndTime }
    val activeList = if (visiblePoints.isNotEmpty()) visiblePoints else sortedPoints

    val firstPt = activeList.first()
    val firstX = ((firstPt.timestamp - effectiveStartTime).toFloat() / totalTimeSpan * width).coerceIn(0f, width)
    val firstY = topPadding + graphHeight * (1f - (firstPt.count - minVal) / valRange)
    if (firstX > 0f) mappedCoordinates.add(Offset(0f, firstY))

    for (pt in activeList) {
        val px = ((pt.timestamp - effectiveStartTime).toFloat() / totalTimeSpan * width).coerceIn(0f, width)
        val py = topPadding + graphHeight * (1f - (pt.count - minVal) / valRange)
        mappedCoordinates.add(Offset(px, py))
    }

    val lastPt = activeList.last()
    val lastX = ((lastPt.timestamp - effectiveStartTime).toFloat() / totalTimeSpan * width).coerceIn(0f, width)
    val lastY = topPadding + graphHeight * (1f - (lastPt.count - minVal) / valRange)
    if (lastX < width) mappedCoordinates.add(Offset(width, lastY))

    if (mappedCoordinates.isNotEmpty()) {
        path.moveTo(mappedCoordinates.first().x, mappedCoordinates.first().y)
        fillPath.moveTo(mappedCoordinates.first().x, topPadding + graphHeight)
        fillPath.lineTo(mappedCoordinates.first().x, mappedCoordinates.first().y)

        for (i in 0 until mappedCoordinates.size - 1) {
            val p1 = mappedCoordinates[i]
            val p2 = mappedCoordinates[i + 1]
            val midX = (p1.x + p2.x) / 2f
            path.cubicTo(midX, p1.y, midX, p2.y, p2.x, p2.y)
            fillPath.cubicTo(midX, p1.y, midX, p2.y, p2.x, p2.y)
        }

        fillPath.lineTo(mappedCoordinates.last().x, topPadding + graphHeight)
        fillPath.close()

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

        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

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
                drawCircle(color = Color.White, radius = 6.dp.toPx(), center = Offset(ptX, ptY))
                drawCircle(color = primaryColor, radius = 4.dp.toPx(), center = Offset(ptX, ptY))
            }
        }
    }
}
