package com.adelinarotaru.fooddelivery.admin.ui.statistics.orders

import com.adelinarotaru.fooddelivery.admin.domain.StatisticsRepository
import com.adelinarotaru.fooddelivery.admin.models.OrderStatistic
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderStatisticsViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val statisticsRepository: StatisticsRepository
) : BaseViewModel() {

    private val _statistics = MutableStateFlow<List<OrderStatistic>?>(null)
    val statistics = _statistics.asStateFlow()

    suspend fun fetchStatistics() = coRunCatching {
//        statisticsRepository.fetchOrderStatistics()
        listOf(
            OrderStatistic("John", "Doe", "2023-06-20", "25.00", true, 1),
            OrderStatistic("Jane", "Smith", "2023-06-19", "18.50", true, 2),
            OrderStatistic("Michael", "Johnson", "2023-06-18", "42.75", false, 3),
            OrderStatistic("Sarah", "Williams", "2023-06-17", "33.20", false, 4),
            OrderStatistic("David", "Brown", "2023-06-16", "56.90", true, 5),
            OrderStatistic("Emily", "Jones", "2023-06-15", "29.80", true, 6)
        )

    }.onSuccess { res ->
        _statistics.value = res
    }.onFailure { sendError(it) }

}