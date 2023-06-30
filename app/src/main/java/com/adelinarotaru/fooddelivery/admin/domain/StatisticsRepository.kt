package com.adelinarotaru.fooddelivery.admin.domain

import com.adelinarotaru.fooddelivery.admin.models.OrderStatistic
import com.adelinarotaru.fooddelivery.admin.models.RevenueStatistics
import com.adelinarotaru.fooddelivery.admin.models.TrendingStatistics

interface StatisticsRepository {
    fun fetchOrderStatistics(): List<OrderStatistic>
    fun fetchRevenueStatistics(): List<RevenueStatistics>
    fun fetchTrendingStatistics(): List<TrendingStatistics>
}
