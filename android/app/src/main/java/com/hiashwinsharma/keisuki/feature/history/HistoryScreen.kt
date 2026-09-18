package com.hiashwinsharma.keisuki.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.LocalAppCornerRadius
import com.hiashwinsharma.keisuki.feature.history.components.CounterPickerBottomSheet
import com.hiashwinsharma.keisuki.feature.history.components.HistoryChartCard
import com.hiashwinsharma.keisuki.feature.history.components.HistoryDatePickerDialog
import com.hiashwinsharma.keisuki.feature.history.components.HistoryDateNavigator
import com.hiashwinsharma.keisuki.feature.history.components.HistoryStatsBento
import com.hiashwinsharma.keisuki.feature.history.components.HistoryTopBar
import com.hiashwinsharma.keisuki.feature.history.components.historyActivityLogSection

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onAction: (HistoryUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentRadius = LocalAppCornerRadius.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showCounterPickerSheet by remember { mutableStateOf(false) }
    var isAutoZoom by remember { mutableStateOf(true) }

    val primaryThemeColor = if (uiState.effectiveColorToken.isCustomColor) {
        uiState.effectiveColorToken.primaryColor
    } else {
        MaterialTheme.colorScheme.primary
    }

    val counterTitle = uiState.selectedCounter?.title ?: "Counter"

    val canZoom = remember(uiState.chartPoints, uiState.startWindowTimestamp, uiState.endWindowTimestamp) {
        if (uiState.chartPoints.size >= 2) {
            val minTime = uiState.chartPoints.minOf { it.timestamp }
            val maxTime = uiState.chartPoints.maxOf { it.timestamp }
            val activeSpan = maxTime - minTime
            val windowSpan = uiState.endWindowTimestamp - uiState.startWindowTimestamp
            activeSpan < windowSpan * 0.7f
        } else false
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HistoryTopBar(
                counterTitle = counterTitle,
                dateDisplayLabel = uiState.dateDisplayLabel,
                isLockedToCounter = uiState.isLockedToCounter,
                hasMultipleCounters = uiState.availableCounters.size > 1,
                primaryThemeColor = primaryThemeColor,
                cornerRadius = currentRadius,
                onBackClick = { onAction(HistoryUiAction.OnBackClick) },
                onJumpToTodayClick = { onAction(HistoryUiAction.OnJumpToToday) },
                onDatePickerClick = { showDatePicker = true },
                onSwitchCounterClick = { showCounterPickerSheet = true }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primaryThemeColor)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. DATE RANGE TABS & SWITCHER
                item {
                    HistoryDateNavigator(
                        selectedMode = uiState.selectedDateRangeMode,
                        dateDisplayLabel = uiState.dateDisplayLabel,
                        primaryThemeColor = primaryThemeColor,
                        cornerRadius = currentRadius,
                        onSelectMode = { onAction(HistoryUiAction.OnSelectRangeMode(it)) },
                        onPreviousPeriod = { onAction(HistoryUiAction.OnPreviousPeriod) },
                        onNextPeriod = { onAction(HistoryUiAction.OnNextPeriod) },
                        onDatePickerClick = { showDatePicker = true }
                    )
                }

                // 2. BATTERY-STYLE GRAPH CARD
                item {
                    HistoryChartCard(
                        counterTitle = counterTitle,
                        chartPoints = uiState.chartPoints,
                        startTime = uiState.startWindowTimestamp,
                        endTime = uiState.endWindowTimestamp,
                        primaryThemeColor = primaryThemeColor,
                        isAutoZoom = isAutoZoom,
                        canZoom = canZoom,
                        cornerRadius = currentRadius,
                        onToggleAutoZoom = { isAutoZoom = !isAutoZoom }
                    )
                }

                // 3. STATS BENTO SUMMARY
                item {
                    HistoryStatsBento(
                        netChange = uiState.netChange,
                        peakCount = uiState.peakCount,
                        totalIncrements = uiState.totalIncrements,
                        totalDecrements = uiState.totalDecrements,
                        primaryThemeColor = primaryThemeColor,
                        cornerRadius = currentRadius
                    )
                }

                // 4. ACTIVITY LOGS NOTIFICATION STACK
                historyActivityLogSection(
                    events = uiState.events,
                    counterTitle = counterTitle,
                    cornerRadius = currentRadius
                )
            }
        }
    }

    if (showCounterPickerSheet && !uiState.isLockedToCounter) {
        CounterPickerBottomSheet(
            availableCounters = uiState.availableCounters,
            selectedCounterId = uiState.selectedCounterId,
            cornerRadius = currentRadius,
            onSelectCounter = { onAction(HistoryUiAction.OnSelectCounter(it)) },
            onDismiss = { showCounterPickerSheet = false }
        )
    }

    if (showDatePicker) {
        HistoryDatePickerDialog(
            initialSelectedDateMillis = uiState.selectedAnchorTimestamp,
            onDateSelected = { onAction(HistoryUiAction.OnSelectCustomDate(it)) },
            onDismiss = { showDatePicker = false }
        )
    }
}
