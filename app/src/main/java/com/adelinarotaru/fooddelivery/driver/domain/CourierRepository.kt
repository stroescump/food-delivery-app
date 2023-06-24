package com.adelinarotaru.fooddelivery.driver.domain

import com.adelinarotaru.fooddelivery.shared.models.Order

interface CourierRepository {
    fun fetchNearbyOrders(): List<Order>
}