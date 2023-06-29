package com.adelinarotaru.fooddelivery.admin

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class AdminViewModel(private val dispatcher: CoroutineDispatcher) : BaseViewModel() {

    fun fetchDashboardStatistics() = viewModelScope.launch(dispatcher) {
        coRunCatching {

        }.onSuccess {

        }.onFailure { sendError(it) }
    }
}