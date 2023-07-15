package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.customer.data.RestaurantResponse
import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import retrofit2.http.GET

interface RestaurantApi {
    @GET("restaurants")
    suspend fun getRestaurants(): List<RestaurantResponse>
}