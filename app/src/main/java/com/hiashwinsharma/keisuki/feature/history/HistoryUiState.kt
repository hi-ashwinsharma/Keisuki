package com.hiashwinsharma.keisuki.feature.history

import com.hiashwinsharma.keisuki.core.model.ColorToken
import com.hiashwinsharma.keisuki.core.model.Counter
import com.hiashwinsharma.keisuki.core.ui.ChartPoint

enum class DateRangeMode(val label: String) {
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month")
}

data class CounterEventDisplay(
    val id: Long,
    val counterId: String,
    val counterTitle: String,
    val colorToken: ColorToken,
    val timestamp: Long,
    val count: Long,
    val delta: Long,
    val eventType: String
)

data class HistoryUiState(
    val selectedCounterId: String = "",
    val isLockedToCounter: Boolean = false,
    val availableCounters: List<Counter> = emptyList(),
    val selectedCounter: Counter? = null,
    val selectedDateRangeMode: DateRangeMode = DateRangeMode.DAY,
    val selectedAnchorTimestamp: Long = System.currentTimeMillis(),
    val startWindowTimestamp: Long = 0L,
    val endWindowTimestamp: Long = 0L,
    val dateDisplayLabel: String = "",
    val chartPoints: List<ChartPoint> = emptyList(),
    val events: List<CounterEventDisplay> = emptyList(),
    val netChange: Long = 0L,
    val totalIncrements: Long = 0L,
    val totalDecrements: Long = 0L,
    val peakCount: Long = 0L,
    val minCount: Long = 0L,
    val effectiveColorToken: ColorToken = ColorToken.DYNAMIC_PRIMARY,
    val isLoading: Boolean = false
)
