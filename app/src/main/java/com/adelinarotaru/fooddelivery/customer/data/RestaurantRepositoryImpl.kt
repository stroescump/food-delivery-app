package com.adelinarotaru.fooddelivery.customer.data

import com.adelinarotaru.fooddelivery.customer.domain.RestaurantRepository
import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import com.adelinarotaru.fooddelivery.shared.networking.RestaurantApi

class RestaurantRepositoryImpl(private val restaurantApi: RestaurantApi) : RestaurantRepository {
    override suspend fun getRestaurants(): List<Restaurant> = restaurantApi.getRestaurants()
}