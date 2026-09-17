package com.hiashwinsharma.keisuki.history

import com.hiashwinsharma.keisuki.core.model.ColorToken
import com.hiashwinsharma.keisuki.core.model.Counter
import com.hiashwinsharma.keisuki.core.model.SyncStatus
import com.hiashwinsharma.keisuki.data.local.CounterEntity
import com.hiashwinsharma.keisuki.data.local.CounterEventEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class CounterHistoryTest {

    @Test
    fun testEventEntityCreation() {
        val now = System.currentTimeMillis()
        val event = CounterEventEntity(
            id = 1L,
            counterId = "counter-1",
            timestamp = now,
            count = 15L,
            delta = 5L,
            eventType = "INCREMENT"
        )

        assertEquals(1L, event.id)
        assertEquals("counter-1", event.counterId)
        assertEquals(now, event.timestamp)
        assertEquals(15L, event.count)
        assertEquals(5L, event.delta)
        assertEquals("INCREMENT", event.eventType)
    }

    @Test
    fun testStatsAggregationLogic() {
        val events = listOf(
            CounterEventEntity(id = 1, counterId = "c1", timestamp = 1000L, count = 1, delta = 1, eventType = "INCREMENT"),
            CounterEventEntity(id = 2, counterId = "c1", timestamp = 2000L, count = 10, delta = 9, eventType = "INCREMENT"),
            CounterEventEntity(id = 3, counterId = "c1", timestamp = 3000L, count = 7, delta = -3, eventType = "DECREMENT"),
            CounterEventEntity(id = 4, counterId = "c1", timestamp = 4000L, count = 0, delta = -7, eventType = "RESET")
        )

        var totalInc = 0L
        var totalDec = 0L
        var netChange = 0L

        events.forEach { event ->
            if (event.delta > 0) totalInc += event.delta
            else if (event.delta < 0) totalDec += Math.abs(event.delta)
            netChange += event.delta
        }

        val peakCount = events.maxOf { it.count }
        val minCount = events.minOf { it.count }

        assertEquals(10L, totalInc)
        assertEquals(10L, totalDec)
        assertEquals(0L, netChange)
        assertEquals(10L, peakCount)
        assertEquals(0L, minCount)
    }

    @Test
    fun testDayWindowBoundaries() {
        val cal = Calendar.getInstance().apply {
            set(2026, Calendar.SEPTEMBER, 18, 14, 30, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val anchor = cal.timeInMillis

        val dayStart = Calendar.getInstance().apply {
            timeInMillis = anchor
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val dayEnd = Calendar.getInstance().apply {
            timeInMillis = anchor
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis

        assertTrue(anchor in dayStart..dayEnd)
        assertEquals(86400000L - 1L, dayEnd - dayStart)
    }

    @Test
    fun testClusteredAutoZoomCalculation() {
        // 5 changes occurring in a 2-minute burst (120_000 ms) inside a 24h window (86_400_000 ms)
        val burstStart = 1700000000000L
        val burstEnd = burstStart + 120_000L
        val activeSpan = burstEnd - burstStart
        val fullDaySpan = 86_400_000L

        val isClustered = activeSpan < fullDaySpan * 0.7f
        assertTrue(isClustered)

        val paddingMs = (activeSpan * 0.20f).toLong().coerceIn(30_000L, 30 * 60_000L)
        val effectiveStart = burstStart - paddingMs
        val effectiveEnd = burstEnd + paddingMs

        assertTrue(effectiveEnd - effectiveStart < 300_000L) // Under 5 minutes framing
    }
}
