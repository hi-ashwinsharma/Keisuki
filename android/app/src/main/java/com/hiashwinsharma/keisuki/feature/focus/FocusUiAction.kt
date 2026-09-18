package com.hiashwinsharma.keisuki.feature.focus

import com.hiashwinsharma.keisuki.core.model.ColorToken

sealed interface FocusUiAction {
    data object OnIncrement : FocusUiAction
    data object OnDecrement : FocusUiAction
    data object OnReset : FocusUiAction
    data class OnUpdateCounter(
        val title: String,
        val count: Long,
        val step: Long,
        val colorToken: ColorToken
    ) : FocusUiAction
    data class OnDelete(val onDeleted: () -> Unit) : FocusUiAction
    data object OnHistoryClick : FocusUiAction
    data object OnBackClick : FocusUiAction
}
