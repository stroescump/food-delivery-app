package com.adelinarotaru.fooddelivery.driver.data

import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.driver.domain.models.OrderStatusResponse
import com.adelinarotaru.fooddelivery.shared.models.Order
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.shared.networking.CourierApi
import com.google.android.gms.maps.model.LatLng

class CourierRepositoryImpl(private val courierApi: CourierApi) : CourierRepository {

    var counter = 0

    private val coordinatesList = listOf(
        LatLng(47.410937, 8.550062),
        LatLng(47.410812, 8.552062),
        LatLng(47.410812, 8.555438),
        LatLng(47.414562, 8.570562),
        LatLng(47.413438, 8.570812),
    )

    private val statusList = listOf(
        OrderStatusResponse(OrderStatus.ORDER_RECEIVED),
        OrderStatusResponse(OrderStatus.PREPARING, "courierId", "Mark John"),
        OrderStatusResponse(OrderStatus.PICKED_UP, "courierId", "Mark John"),
        OrderStatusResponse(OrderStatus.PICKED_UP, "courierId", "Mark John"),
        OrderStatusResponse(OrderStatus.DELIVERED, "courierId", "Mark John")
    )

    var statusCounter = 0

    override suspend fun fetchNearbyOrders(): List<Order> = courierApi.fetchNearbyOrder()
    override suspend fun trackOrder(orderId: String): OrderStatusResponse {
        statusCounter += 1
        return statusList[statusCounter - 1]
    }

    override suspend fun fetchCourierCoordinates(orderId: String): LatLng {
        val coord = coordinatesList[counter]
        counter += 1
        return coord
//        return LatLng(47.411062, 8.546062)
    }
}