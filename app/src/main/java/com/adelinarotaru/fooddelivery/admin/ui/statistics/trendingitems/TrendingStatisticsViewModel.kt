package com.adelinarotaru.fooddelivery.admin.ui.statistics.trendingitems

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.admin.domain.StatisticsRepository
import com.adelinarotaru.fooddelivery.admin.models.TrendingStatistics
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.CuisineType
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
//            statisticsRepository.fetchTrendingStatistics()
            listOf(
                TrendingStatistics("Pizza Calzone", CuisineType.Pizza.default, 20, 0),
                TrendingStatistics("Pizza Calzone", CuisineType.Pizza.default, 20, 1),
                TrendingStatistics("Pizza Calzone", CuisineType.Pizza.default, 20, 2),
                TrendingStatistics("Pizza Calzone", CuisineType.Pizza.default, 20, 3),
                TrendingStatistics("Pizza Calzone", CuisineType.Pizza.default, 20, 4),
                TrendingStatistics("Pizza Calzone", CuisineType.Pizza.default, 20, 5),
            )
        }.onSuccess {
            _statistics.value = it
        }.onFailure { sendError(it) }
    }
}