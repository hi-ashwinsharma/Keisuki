package com.hiashwinsharma.keisuki.core.ui.chart

import com.hiashwinsharma.keisuki.core.ui.ChartPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

object ChartMathUtils {

    fun calculateEffectiveFraming(
        sortedPoints: List<ChartPoint>,
        startTime: Long,
        endTime: Long,
        isAutoZoom: Boolean
    ): Pair<Long, Long> {
        if (!isAutoZoom || sortedPoints.size < 2) {
            return Pair(startTime, endTime)
        }

        val minPointTime = sortedPoints.first().timestamp
        val maxPointTime = sortedPoints.last().timestamp
        val activeSpan = (maxPointTime - minPointTime).coerceAtLeast(1000L)
        val windowSpan = endTime - startTime

        // If activity is concentrated within less than 70% of the window
        return if (activeSpan < windowSpan * 0.7f) {
            val paddingMs = (activeSpan * 0.20f).toLong().coerceIn(30_000L, 30 * 60_000L)
            val zStart = (minPointTime - paddingMs).coerceAtLeast(startTime)
            val zEnd = (maxPointTime + paddingMs).coerceAtMost(endTime)
            Pair(zStart, zEnd)
        } else {
            Pair(startTime, endTime)
        }
    }

    fun findClosestPoint(
        touchX: Float,
        canvasWidth: Float,
        points: List<ChartPoint>,
        startTime: Long,
        endTime: Long
    ): ChartPoint? {
        if (points.isEmpty() || canvasWidth <= 0f) return null
        val totalTime = (endTime - startTime).coerceAtLeast(1L)
        val touchTimestamp = startTime + ((touchX / canvasWidth).coerceIn(0f, 1f) * totalTime).toLong()

        return points.minByOrNull { abs(it.timestamp - touchTimestamp) }
    }

    fun getTimeFormatPattern(totalTimeSpan: Long): String {
        return when {
            totalTimeSpan <= 10 * 60 * 1000L -> "h:mm:ss a"   // <= 10 mins: seconds
            totalTimeSpan <= 6 * 3600 * 1000L -> "h:mm a"     // <= 6 hours: minute precision
            totalTimeSpan <= 48 * 3600 * 1000L -> "h a"       // <= 2 days: hour
            else -> "MMM d"                                   // Multiday / week / month
        }
    }

    fun formatMarkerTime(markerTime: Long, pattern: String): String {
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(Date(markerTime))
    }
}
