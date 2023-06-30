package com.adelinarotaru.fooddelivery.admin.ui.statistics.revenue

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.admin.domain.StatisticsRepository
import com.adelinarotaru.fooddelivery.admin.models.RevenueStatistics
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RevenueStatisticsViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val statisticsRepository: StatisticsRepository
) : BaseViewModel() {

    private val _statistics = MutableStateFlow<List<RevenueStatistics>?>(null)
    val statistics = _statistics.asStateFlow()

    fun fetchStatistics() = viewModelScope.launch(dispatcher) {
        coRunCatching {
            listOf(
                RevenueStatistics(200f, 0),
                RevenueStatistics(520f, 1),
                RevenueStatistics(840f, 2),
                RevenueStatistics(124f, 3),
                RevenueStatistics(1002f, 4),
                RevenueStatistics(1241f, 5),
                RevenueStatistics(1923f, 6),
            )
//            statisticsRepository.fetchRevenueStatistics()
        }.onSuccess {
            _statistics.value = it
        }.onFailure { sendError(it) }
    }
}