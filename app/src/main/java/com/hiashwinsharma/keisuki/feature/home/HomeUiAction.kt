package com.hiashwinsharma.keisuki.feature.home

import com.hiashwinsharma.keisuki.core.model.ColorToken
import com.hiashwinsharma.keisuki.data.preferences.CounterSortOrder

sealed interface HomeUiAction {
    data class OnCounterClick(val counterId: String) : HomeUiAction
    data class OnIncrement(val counterId: String) : HomeUiAction
    data class OnDecrement(val counterId: String) : HomeUiAction
    data class OnCreateCounter(
        val title: String,
        val initialCount: Long,
        val step: Long,
        val colorToken: ColorToken
    ) : HomeUiAction
    data class OnSetSortOrder(val order: CounterSortOrder) : HomeUiAction
    data object OnSettingsClick : HomeUiAction
    data class OnSignInWithGoogle(val webClientId: String) : HomeUiAction
    data object OnSignOut : HomeUiAction
}
