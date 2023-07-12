package com.adelinarotaru.fooddelivery.driver.domain

import com.adelinarotaru.fooddelivery.driver.models.ICourierOrderAccepted
import com.adelinarotaru.fooddelivery.driver.models.OrderStatusResponse
import com.adelinarotaru.fooddelivery.shared.models.Order
import com.google.android.gms.maps.model.LatLng

interface CourierRepository {
    suspend fun fetchNearbyOrders(): List<Order>
    suspend fun trackOrder(orderId: String): OrderStatusResponse
    suspend fun fetchCourierCoordinates(orderId: String): LatLng
    suspend fun fetchOrderDetails(orderId: String): ICourierOrderAccepted
}