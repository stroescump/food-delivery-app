package com.adelinarotaru.fooddelivery.customer

import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import com.adelinarotaru.fooddelivery.shared.networking.RestaurantApi

interface RestaurantRepository {
    suspend fun getRestaurants(): List<Restaurant>
}

class RestaurantRepositoryImpl(private val restaurantApi: RestaurantApi) : RestaurantRepository {
    override suspend fun getRestaurants(): List<Restaurant> = restaurantApi.getRestaurants()
}
