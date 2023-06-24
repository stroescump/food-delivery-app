package com.adelinarotaru.fooddelivery.driver.data

import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.shared.models.Order
import com.adelinarotaru.fooddelivery.shared.networking.CourierApi

class CourierRepositoryImpl(private val courierApi: CourierApi) : CourierRepository {
    override fun fetchNearbyOrders(): List<Order> = courierApi.fetchNearbyOrder()
}