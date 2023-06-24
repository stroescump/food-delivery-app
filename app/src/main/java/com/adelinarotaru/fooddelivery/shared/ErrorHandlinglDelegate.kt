package com.adelinarotaru.fooddelivery.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ErrorHandlinglDelegate {
    val error: StateFlow<Throwable?>
    fun sendError(error: Throwable)
}

class ErrorHandlingDelegateImpl : ErrorHandlinglDelegate {
    private val _error = MutableStateFlow<Throwable?>(null)
    override val error: StateFlow<Throwable?> = _error.asStateFlow()

    override fun sendError(error: Throwable) {
        _error.value = error
    }
}