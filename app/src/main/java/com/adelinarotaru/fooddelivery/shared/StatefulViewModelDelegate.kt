package com.adelinarotaru.fooddelivery.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BaseUIState(val error: Throwable? = null, val loading: Boolean = false)

interface StatefulViewModelDelegate {
    val uiState: StateFlow<BaseUIState>
    fun sendError(error: Throwable)
    fun setLoading(isLoading: Boolean)
}

class StatefulViewModelDelegateImpl : StatefulViewModelDelegate {
    private val _uiState = MutableStateFlow(BaseUIState())
    override val uiState: StateFlow<BaseUIState> = _uiState.asStateFlow()

    override fun sendError(error: Throwable) {
        _uiState.update { it.copy(error = error) }
    }

    override fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(loading = isLoading) }
    }
}