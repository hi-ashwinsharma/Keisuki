package com.hiashwinsharma.keisuki.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiashwinsharma.keisuki.core.model.ColorToken
import com.hiashwinsharma.keisuki.core.model.Counter
import com.hiashwinsharma.keisuki.core.ui.ChartPoint
import com.hiashwinsharma.keisuki.data.local.CounterEventEntity
import com.hiashwinsharma.keisuki.data.repository.CounterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModel(
    initialCounterId: String,
    private val counterRepository: CounterRepository
) : ViewModel() {

    private val isLockedToCounter = initialCounterId != "all"
    private val _selectedCounterId = MutableStateFlow(if (isLockedToCounter) initialCounterId else "")
    private val _rangeMode = MutableStateFlow(DateRangeMode.DAY)
    private val _anchorTimestamp = MutableStateFlow(System.currentTimeMillis())

    private val timeWindowFlow = combine(_rangeMode, _anchorTimestamp) { mode, anchor ->
        DateWindowUtils.calculateWindow(mode, anchor)
    }

    private val rawEventsFlow = timeWindowFlow.flatMapLatest { window ->
        counterRepository.getAllEventsBetween(window.startTime, window.endTime)
    }

    val uiState: StateFlow<HistoryUiState> = combine(
        counterRepository.countersFlow,
        rawEventsFlow,
        timeWindowFlow,
        _selectedCounterId
    ) { counters: List<Counter>, rawEvents: List<CounterEventEntity>, window: TimeWindowConfig, selectedId: String ->
        val effectiveSelectedId = when {
            isLockedToCounter -> initialCounterId
            selectedId.isNotEmpty() && counters.any { it.id == selectedId } -> selectedId
            counters.isNotEmpty() -> counters.first().id
            else -> ""
        }

        val counterMap = counters.associateBy { it.id }
        val selectedCounter = counterMap[effectiveSelectedId]
        val effectiveColor = selectedCounter?.colorToken ?: ColorToken.DYNAMIC_PRIMARY

        val filteredRawEvents = if (effectiveSelectedId.isNotEmpty()) {
            rawEvents.filter { it.counterId == effectiveSelectedId }
        } else {
            emptyList()
        }

        val displayEvents = filteredRawEvents.map { entity ->
            val c = counterMap[entity.counterId]
            CounterEventDisplay(
                id = entity.id,
                counterId = entity.counterId,
                counterTitle = c?.title ?: "Counter",
                colorToken = c?.colorToken ?: ColorToken.DYNAMIC_PRIMARY,
                timestamp = entity.timestamp,
                count = entity.count,
                delta = entity.delta,
                eventType = entity.eventType
            )
        }

        var totalInc = 0L
        var totalDec = 0L
        var netChange = 0L

        filteredRawEvents.forEach { event ->
            if (event.delta > 0) totalInc += event.delta
            else if (event.delta < 0) totalDec += Math.abs(event.delta)
            netChange += event.delta
        }

        val peakCount = if (filteredRawEvents.isNotEmpty()) {
            filteredRawEvents.maxOf { it.count }
        } else {
            selectedCounter?.count ?: 0L
        }

        val minCount = if (filteredRawEvents.isNotEmpty()) {
            filteredRawEvents.minOf { it.count }
        } else {
            selectedCounter?.count ?: 0L
        }

        val points = mutableListOf<ChartPoint>()
        if (filteredRawEvents.isNotEmpty()) {
            filteredRawEvents.forEach { e ->
                points.add(ChartPoint(timestamp = e.timestamp, count = e.count))
            }
        } else if (selectedCounter != null) {
            points.add(ChartPoint(timestamp = window.startTime, count = selectedCounter.count))
            points.add(ChartPoint(timestamp = window.endTime, count = selectedCounter.count))
        }

        HistoryUiState(
            selectedCounterId = effectiveSelectedId,
            isLockedToCounter = isLockedToCounter,
            availableCounters = counters,
            selectedCounter = selectedCounter,
            selectedDateRangeMode = window.mode,
            selectedAnchorTimestamp = window.anchor,
            startWindowTimestamp = window.startTime,
            endWindowTimestamp = window.endTime,
            dateDisplayLabel = window.label,
            chartPoints = points,
            events = displayEvents.reversed(),
            netChange = netChange,
            totalIncrements = totalInc,
            totalDecrements = totalDec,
            peakCount = peakCount,
            minCount = minCount,
            effectiveColorToken = effectiveColor,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState(
            selectedCounterId = if (isLockedToCounter) initialCounterId else "",
            isLockedToCounter = isLockedToCounter,
            isLoading = true
        )
    )

    fun onAction(action: HistoryUiAction) {
        when (action) {
            is HistoryUiAction.OnSelectCounter -> {
                if (!isLockedToCounter) {
                    _selectedCounterId.value = action.counterId
                }
            }
            is HistoryUiAction.OnSelectRangeMode -> {
                _rangeMode.value = action.mode
            }
            HistoryUiAction.OnPreviousPeriod -> {
                _anchorTimestamp.value = DateWindowUtils.shiftPeriod(_rangeMode.value, _anchorTimestamp.value, -1)
            }
            HistoryUiAction.OnNextPeriod -> {
                _anchorTimestamp.value = DateWindowUtils.shiftPeriod(_rangeMode.value, _anchorTimestamp.value, 1)
            }
            HistoryUiAction.OnJumpToToday -> {
                _anchorTimestamp.value = System.currentTimeMillis()
            }
            is HistoryUiAction.OnSelectCustomDate -> {
                _anchorTimestamp.value = action.epochMs
            }
            HistoryUiAction.OnBackClick -> {}
        }
    }
}
