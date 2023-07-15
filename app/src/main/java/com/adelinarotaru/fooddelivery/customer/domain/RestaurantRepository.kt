package com.adelinarotaru.fooddelivery.customer.domain

import com.adelinarotaru.fooddelivery.customer.data.MenuItemResponse
import com.adelinarotaru.fooddelivery.customer.data.RestaurantResponse

interface RestaurantRepository {
    suspend fun getRestaurants(): List<RestaurantResponse>
}

