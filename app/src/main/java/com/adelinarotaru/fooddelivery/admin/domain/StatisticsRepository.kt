package com.adelinarotaru.fooddelivery.admin.domain

import com.adelinarotaru.fooddelivery.admin.models.OrderStatistic
import com.adelinarotaru.fooddelivery.admin.models.RevenueStatistics
import com.adelinarotaru.fooddelivery.admin.models.TrendingStatistics

interface StatisticsRepository {
    fun fetchOrderStatistics(): OrderStatistic
    fun fetchRevenueStatistics(): RevenueStatistics
    fun fetchTrendingStatistics(): TrendingStatistics
}
