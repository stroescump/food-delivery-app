package com.adelinarotaru.fooddelivery.customer.domain

import com.adelinarotaru.fooddelivery.shared.models.Restaurant

interface RestaurantRepository {
    suspend fun getRestaurants(): List<Restaurant>
}

