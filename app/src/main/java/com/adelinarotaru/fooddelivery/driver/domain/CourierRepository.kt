package com.adelinarotaru.fooddelivery.driver.domain

import com.adelinarotaru.fooddelivery.driver.models.OrderStatusResponse
import com.adelinarotaru.fooddelivery.driver.ui.dashboard.CourierItemTask
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.domain.AcceptOrderRequest
import com.adelinarotaru.fooddelivery.shared.login.domain.ILocation
import com.adelinarotaru.fooddelivery.shared.models.GenericResponse
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus

interface CourierRepository {
    suspend fun fetchNearbyOrders(orderStatus: OrderStatus): List<CourierItemTask>
    suspend fun fetchAcceptedOrders(courierId: String): List<CourierItemTask>
    suspend fun fetchAllOrders(courierId: String): List<CourierItemTask>
    suspend fun trackOrder(orderId: String): OrderStatusResponse
    suspend fun fetchCourierCoordinates(orderId: String): ILocation
    suspend fun fetchCourierCheckpoints(orderId: String): List<ILocation>
    suspend fun fetchCustomerCheckpoints(orderId: String): List<ILocation>
    suspend fun updateOrderStatus(orderId: String, orderStatus: OrderStatus): GenericResponse
    suspend fun convertAddressToCoordinates(address: String): ILocation
    suspend fun acceptOrder(orderId: String, acceptOrderRequest: AcceptOrderRequest): GenericResponse
}