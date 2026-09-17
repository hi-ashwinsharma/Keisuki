package com.hiashwinsharma.keisuki.feature.history

sealed interface HistoryUiAction {
    data class OnSelectCounter(val counterId: String) : HistoryUiAction
    data class OnSelectRangeMode(val mode: DateRangeMode) : HistoryUiAction
    data object OnPreviousPeriod : HistoryUiAction
    data object OnNextPeriod : HistoryUiAction
    data object OnJumpToToday : HistoryUiAction
    data class OnSelectCustomDate(val epochMs: Long) : HistoryUiAction
    data object OnBackClick : HistoryUiAction
}
