package com.hiashwinsharma.keisuki.feature.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiashwinsharma.keisuki.core.model.ColorToken
import com.hiashwinsharma.keisuki.core.model.Counter
import com.hiashwinsharma.keisuki.data.preferences.UserPreferences
import com.hiashwinsharma.keisuki.data.preferences.UserPreferencesRepository
import com.hiashwinsharma.keisuki.data.repository.CounterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FocusViewModel(
    private val counterId: String,
    private val counterRepository: CounterRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val counterFlow: StateFlow<Counter?> = counterRepository
        .getCounterByIdFlow(counterId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val preferences: StateFlow<UserPreferences> = preferencesRepository.preferences

    fun onAction(action: FocusUiAction) {
        when (action) {
            is FocusUiAction.OnIncrement -> increment()
            is FocusUiAction.OnDecrement -> decrement()
            is FocusUiAction.OnReset -> reset()
            is FocusUiAction.OnUpdateCounter -> updateCounter(
                title = action.title,
                count = action.count,
                step = action.step,
                colorToken = action.colorToken
            )
            is FocusUiAction.OnDelete -> deleteCounter(action.onDeleted)
            is FocusUiAction.OnBackClick -> Unit
        }
    }

    fun increment() {
        viewModelScope.launch {
            counterRepository.incrementCounter(counterId)
        }
    }

    fun decrement() {
        viewModelScope.launch {
            counterRepository.decrementCounter(counterId)
        }
    }

    fun reset() {
        viewModelScope.launch {
            counterRepository.resetCounter(counterId)
        }
    }

    fun updateCounter(title: String, count: Long, step: Long, colorToken: ColorToken) {
        viewModelScope.launch {
            counterRepository.updateCounter(
                id = counterId,
                title = title,
                count = count,
                step = step,
                colorToken = colorToken
            )
        }
    }

    fun deleteCounter(onDeleted: () -> Unit) {
        viewModelScope.launch {
            counterRepository.deleteCounter(counterId)
            onDeleted()
        }
    }
}
