package com.adelinarotaru.fooddelivery.admin.data

import com.adelinarotaru.fooddelivery.admin.domain.StatisticsRepository
import com.adelinarotaru.fooddelivery.admin.models.OrderStatistic
import com.adelinarotaru.fooddelivery.admin.models.RevenueStatistics
import com.adelinarotaru.fooddelivery.admin.models.TrendingStatistics
import com.adelinarotaru.fooddelivery.shared.networking.StatisticsApi

class StatisticsRepositoryImpl(private val statisticsApi: StatisticsApi) : StatisticsRepository {
    override fun fetchOrderStatistics(): List<OrderStatistic> =
        statisticsApi.fetchOrderStatistics()


    override fun fetchRevenueStatistics(): List<RevenueStatistics> =
        statisticsApi.fetchRevenueStatistics()


    override fun fetchTrendingStatistics(): List<TrendingStatistics> =
        statisticsApi.fetchTrendingStatistics()

}