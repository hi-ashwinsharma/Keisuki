package com.hiashwinsharma.keisuki.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiashwinsharma.keisuki.data.auth.AuthRepository
import com.hiashwinsharma.keisuki.data.auth.AuthState
import com.hiashwinsharma.keisuki.data.preferences.CounterSortOrder
import com.hiashwinsharma.keisuki.data.preferences.UserPreferences
import com.hiashwinsharma.keisuki.data.preferences.UserPreferencesRepository
import com.hiashwinsharma.keisuki.data.repository.CounterRepository
import com.hiashwinsharma.keisuki.model.ColorToken
import com.hiashwinsharma.keisuki.model.Counter
import com.hiashwinsharma.keisuki.model.SyncStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val counters: List<Counter> = emptyList(),
    val authState: AuthState = AuthState(),
    val preferences: UserPreferences = UserPreferences(),
    val isSyncPending: Boolean = false,
    val isLoading: Boolean = false
)

class CounterGridViewModel(
    private val counterRepository: CounterRepository,
    private val authRepository: AuthRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        counterRepository.countersFlow,
        authRepository.authState,
        preferencesRepository.preferences
    ) { counters, authState, prefs ->
        val hasPending = authState.isAuthenticated && counters.any { it.syncStatus == SyncStatus.PENDING_SYNC }
        val sortedCounters = when (prefs.sortOrder) {
            CounterSortOrder.LATEST_CHANGE -> counters.sortedWith(
                compareByDescending<Counter> { it.updatedAt }.thenByDescending { it.createdAt }
            )
            CounterSortOrder.CREATION_DATE -> counters.sortedWith(
                compareByDescending<Counter> { it.createdAt }.thenByDescending { it.updatedAt }
            )
            CounterSortOrder.NAME -> counters.sortedWith(
                compareBy(String.CASE_INSENSITIVE_ORDER) { it.title }
            )
            CounterSortOrder.COUNT -> counters.sortedWith(
                compareByDescending<Counter> { it.count }.thenByDescending { it.updatedAt }
            )
        }
        HomeUiState(
            counters = sortedCounters,
            authState = authState,
            preferences = prefs,
            isSyncPending = hasPending,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeUiState(isLoading = true)
    )

    fun setSortOrder(order: CounterSortOrder) {
        preferencesRepository.setSortOrder(order)
    }

    fun createCounter(
        title: String,
        initialCount: Long,
        step: Long,
        colorToken: ColorToken
    ) {
        viewModelScope.launch {
            counterRepository.createCounter(
                title = title,
                initialCount = initialCount,
                step = step,
                colorToken = colorToken
            )
        }
    }

    fun increment(id: String) {
        viewModelScope.launch {
            counterRepository.increment(id)
        }
    }

    fun decrement(id: String) {
        viewModelScope.launch {
            counterRepository.decrement(id)
        }
    }

    fun deleteCounter(id: String) {
        viewModelScope.launch {
            counterRepository.delete(id)
        }
    }

    fun signInWithGoogle(webClientId: String) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(webClientId)
            result.onSuccess { user ->
                counterRepository.onUserSignedIn(user.uid)
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
