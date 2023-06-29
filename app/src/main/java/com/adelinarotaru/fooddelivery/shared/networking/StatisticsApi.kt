package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.admin.models.OrderStatistic
import com.adelinarotaru.fooddelivery.admin.models.RevenueStatistics
import com.adelinarotaru.fooddelivery.admin.models.TrendingStatistics
import retrofit2.http.GET

interface StatisticsApi {

    @GET("/statistics/orders ")
    fun fetchOrderStatistics(): OrderStatistic

    @GET("/statistics/revenue")
    fun fetchRevenueStatistics(): RevenueStatistics

    @GET("/statistics/trending")
    fun fetchTrendingStatistics(): TrendingStatistics
}
