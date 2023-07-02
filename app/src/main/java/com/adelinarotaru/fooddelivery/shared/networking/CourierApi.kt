package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.shared.models.Order
import retrofit2.http.GET

interface CourierApi {
    @GET("/courier/nearbyorders")
    fun fetchNearbyOrder(): List<Order>
}