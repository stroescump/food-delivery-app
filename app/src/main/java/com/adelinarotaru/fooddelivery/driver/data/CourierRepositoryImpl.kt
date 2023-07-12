package com.adelinarotaru.fooddelivery.driver.data

import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.driver.models.CourierOrderAcceptedResponse
import com.adelinarotaru.fooddelivery.driver.models.ICourierOrderAccepted
import com.adelinarotaru.fooddelivery.driver.models.OrderStatusResponse
import com.adelinarotaru.fooddelivery.shared.models.MenuItem
import com.adelinarotaru.fooddelivery.shared.models.Order
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.shared.models.Restaurant
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

    override suspend fun fetchOrderDetails(orderId: String): ICourierOrderAccepted =
        CourierOrderAcceptedResponse(
            productsOrdered = listOf(
                MenuItem(
                    0, "Burger", 22.4, 2001,
                    Restaurant(
                        id = 2001,
                        name = "Burger Paradise",
                        lat = "47.404187",
                        long = "8.557187",
                        rating = 4.5f,
                        phoneNumber = "+41 782 445 349"
                    ),
                    "Burger",
                ),

                MenuItem(
                    id = 1,
                    name = "Margherita Pizza",
                    price = 9.99,
                    restaurantId = 1,
                    restaurant = Restaurant(
                        id = 2001,
                        name = "Burger Paradise",
                        lat = "47.391562",
                        long = "8.506813",
                        rating = 4.5f,
                        phoneNumber = "+41 782 445 349"
                    ),
                    foodCategory = "Pizza",
                    ingredients = listOf("Tomato Sauce", "Mozzarella Cheese", "Basil"),
                    description = "Classic pizza topped with tomato sauce, cheese, and fresh basil."
                ),

                MenuItem(
                    id = 2,
                    name = "Chicken Alfredo",
                    price = 12.99,
                    restaurantId = 2,
                    restaurant = Restaurant(
                        id = 2001,
                        name = "Burger Paradise",
                        lat = "47.436187",
                        long = "8.568313",
                        rating = 4.5f,
                        phoneNumber = "+41 782 445 349"
                    ),
                    foodCategory = "Pasta",
                    ingredients = listOf("Grilled Chicken", "Alfredo Sauce", "Pasta"),
                    description = "Creamy pasta dish with grilled chicken in Alfredo sauce."
                ),

                MenuItem(
                    3, "Pizza Quattro Formaggi", 21.2, 2001,
                    Restaurant(
                        id = 2001,
                        name = "Burger Paradise",
                        lat = "47.408062",
                        long = "8.595562",
                        rating = 4.5f,
                        phoneNumber = "+41 782 445 349"
                    ),
                    "Pizza",
                )
            )
        )

    override suspend fun markOrderDelivered(orderId: String): Boolean {
        return true
    }
}