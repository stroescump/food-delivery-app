package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.admin.models.OrderStatistic
import com.adelinarotaru.fooddelivery.admin.models.RevenueStatistics
import com.adelinarotaru.fooddelivery.admin.models.TrendingStatistics
import retrofit2.http.GET

interface StatisticsApi {

    @GET("/statistics/orders ")
    fun fetchOrderStatistics(): List<OrderStatistic>

    @GET("/statistics/revenue")
    fun fetchRevenueStatistics(): List<RevenueStatistics>

    @GET("/statistics/trending")
    fun fetchTrendingStatistics(): List<TrendingStatistics>
}
