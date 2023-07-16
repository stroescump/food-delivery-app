package com.adelinarotaru.fooddelivery.driver.data

import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.driver.models.OrderStatusResponse
import com.adelinarotaru.fooddelivery.driver.ui.dashboard.CourierItemTask
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.data.models.AddressToCoordinatesRequest
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.domain.UpdateOrderStatusRequest
import com.adelinarotaru.fooddelivery.shared.login.domain.ILocation
import com.adelinarotaru.fooddelivery.shared.models.GenericResponse
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.shared.networking.CourierApi

class CourierRepositoryImpl(private val courierApi: CourierApi) : CourierRepository {

    override suspend fun fetchNearbyOrders(orderStatus: OrderStatus): List<CourierItemTask> =
        courierApi.fetchNearbyOrder(orderStatus.orderStep)

    override suspend fun fetchAcceptedOrders(courierId: String): List<CourierItemTask> =
        courierApi.fetchAcceptedOrders(courierId)

    override suspend fun fetchAllOrders(courierId: String): List<CourierItemTask> =
        courierApi.fetchAllOrders(courierId)

    override suspend fun trackOrder(orderId: String): OrderStatusResponse =
        courierApi.trackOrder(orderId)

    override suspend fun fetchCourierCoordinates(orderId: String) =
        courierApi.fetchLiveCourierUpdates(orderId)

    override suspend fun fetchCourierCheckpoints(orderId: String) =
        courierApi.fetchCourierCheckpoints(orderId)

    override suspend fun markOrderDelivered(orderId: String): GenericResponse =
        courierApi.updateOrderStatus(
            status = UpdateOrderStatusRequest(OrderStatus.DELIVERED.orderStep),
            orderId = orderId
        )

    override suspend fun convertAddressToCoordinates(address: String): ILocation =
        courierApi.convertAddressToCoordinates(AddressToCoordinatesRequest(address))
}