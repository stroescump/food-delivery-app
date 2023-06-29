package com.adelinarotaru.fooddelivery.admin.ui.statistics.revenue

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class RevenueStatisticsViewModel(private val dispatcher: CoroutineDispatcher) : BaseViewModel() {

    fun fetchStatistics() = viewModelScope.launch(dispatcher) {
        coRunCatching {

        }.onSuccess {

        }.onFailure { sendError(it) }
    }
}