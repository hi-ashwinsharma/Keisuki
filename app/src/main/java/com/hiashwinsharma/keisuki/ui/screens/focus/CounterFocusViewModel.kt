package com.hiashwinsharma.keisuki.ui.screens.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiashwinsharma.keisuki.data.preferences.UserPreferences
import com.hiashwinsharma.keisuki.data.preferences.UserPreferencesRepository
import com.hiashwinsharma.keisuki.data.repository.CounterRepository
import com.hiashwinsharma.keisuki.model.ColorToken
import com.hiashwinsharma.keisuki.model.Counter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CounterFocusViewModel(
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

    fun increment() {
        viewModelScope.launch {
            counterRepository.increment(counterId)
        }
    }

    fun decrement() {
        viewModelScope.launch {
            counterRepository.decrement(counterId)
        }
    }

    fun reset() {
        viewModelScope.launch {
            counterRepository.reset(counterId)
        }
    }

    fun updateCounter(title: String, count: Long, step: Long, colorToken: ColorToken) {
        viewModelScope.launch {
            counterRepository.updateCounter(counterId, title, count, step, colorToken)
        }
    }

    fun deleteCounter(onDeleted: () -> Unit) {
        viewModelScope.launch {
            counterRepository.delete(counterId)
            onDeleted()
        }
    }
}
