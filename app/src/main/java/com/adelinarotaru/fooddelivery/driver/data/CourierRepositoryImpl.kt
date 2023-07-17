package com.adelinarotaru.fooddelivery.driver.data

import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.driver.models.OrderStatusResponse
import com.adelinarotaru.fooddelivery.driver.ui.dashboard.CourierItemTask
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.data.models.AddressToCoordinatesRequest
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.data.models.BatchAddressResponse
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.data.models.BatchCoordinatesRequest
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.data.models.CoordinatesRequest
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.domain.AcceptOrderRequest
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

    override suspend fun fetchCustomerCheckpoints(orderId: String): List<ILocation> =
        courierApi.fetchCustomerCheckpoints(orderId)

    override suspend fun updateOrderStatus(
        orderId: String,
        orderStatus: OrderStatus
    ): GenericResponse =
        courierApi.updateOrderStatus(
            status = UpdateOrderStatusRequest(orderStatus.orderStep),
            orderId = orderId
        )

    override suspend fun convertAddressToCoordinates(address: String): ILocation =
        courierApi.convertAddressToCoordinates(AddressToCoordinatesRequest(address))

    override suspend fun acceptOrder(
        orderId: String,
        acceptOrderRequest: AcceptOrderRequest
    ): GenericResponse = courierApi.acceptOrder(orderId, acceptOrderRequest)

    override suspend fun sendCourierLocationUpdates(
        userId: String,
        coordinatesRequest: CoordinatesRequest
    ): GenericResponse = courierApi.sendLocationUpdates(userId, coordinatesRequest)

    override suspend fun getAddressFromCoordinates(coordinatesRequest: BatchCoordinatesRequest): BatchAddressResponse =
        courierApi.getAddressFromCoordinates(coordinatesRequest)
}