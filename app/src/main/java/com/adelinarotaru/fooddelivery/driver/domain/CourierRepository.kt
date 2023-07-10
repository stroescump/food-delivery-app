package com.adelinarotaru.fooddelivery.driver.domain

import com.adelinarotaru.fooddelivery.shared.models.Order
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.google.android.gms.maps.model.LatLng

interface CourierRepository {
    suspend fun fetchNearbyOrders(): List<Order>
    suspend fun trackOrder(orderId: String): OrderStatus
    suspend fun fetchCourierCoordinates(): LatLng
}