package com.adelinarotaru.fooddelivery.admin.ui.statistics.trendingitems

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.admin.domain.StatisticsRepository
import com.adelinarotaru.fooddelivery.admin.models.TrendingStatistics
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrendingStatisticsViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val statisticsRepository: StatisticsRepository
) : BaseViewModel() {

    private val _statistics = MutableStateFlow<List<TrendingStatistics>?>(null)
    val statistics = _statistics.asStateFlow()
    fun fetchStatistics() = viewModelScope.launch(dispatcher) {
        coRunCatching {
            statisticsRepository.fetchTrendingStatistics()
        }.onSuccess {
            _statistics.value = it
        }.onFailure { sendError(it) }
    }
}