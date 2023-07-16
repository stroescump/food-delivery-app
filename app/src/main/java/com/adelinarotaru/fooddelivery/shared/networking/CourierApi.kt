package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.driver.data.models.CourierLocationResponse
import com.adelinarotaru.fooddelivery.driver.data.models.RestaurantCheckpoint
import com.adelinarotaru.fooddelivery.driver.models.OrderStatusResponse
import com.adelinarotaru.fooddelivery.driver.ui.dashboard.CourierItemTask
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.data.models.AddressToCoordinatesRequest
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.data.models.CustomerCoordinates
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.domain.UpdateOrderStatusRequest
import com.adelinarotaru.fooddelivery.shared.models.GenericResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CourierApi {
    @GET("/orders/getOrdersByStatus/{orderStatus}")
    suspend fun fetchNearbyOrder(@Path("orderStatus") orderStatus: Int): List<CourierItemTask>

    @GET("/orders/getUpdates/{orderId}")
    suspend fun trackOrder(@Path("orderId") orderId: String): OrderStatusResponse

    @GET("/orders/{orderId}/courierLocation")
    suspend fun fetchLiveCourierUpdates(@Path("orderId") orderId: String): CourierLocationResponse

    @GET("/orders/{orderId}/getCheckpoints")
    suspend fun fetchCourierCheckpoints(@Path("orderId") orderId: String): List<RestaurantCheckpoint>

    @POST("/addressToCoordinates")
    suspend fun convertAddressToCoordinates(@Body request: AddressToCoordinatesRequest): CustomerCoordinates

    @PUT("/orders/{orderId}/updateStatus")
    suspend fun updateOrderStatus(
        @Body status: UpdateOrderStatusRequest,
        @Path("orderId") orderId: String
    ): GenericResponse

    @GET("/orders/acceptedOrders/{courierId}")
    suspend fun fetchAcceptedOrders(@Path("courierId") courierId: String): List<CourierItemTask>

    @GET("/orders/allOrders/{courierId}")
    suspend fun fetchAllOrders(@Path("courierId") courierId: String): List<CourierItemTask>
}