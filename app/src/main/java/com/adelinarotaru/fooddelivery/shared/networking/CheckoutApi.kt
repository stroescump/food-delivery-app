package com.adelinarotaru.fooddelivery.shared.networking

import com.adelinarotaru.fooddelivery.customer.checkout.data.CheckoutResponse
import com.adelinarotaru.fooddelivery.customer.checkout.models.OrderRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CheckoutApi {
    @POST("/users/{userId}/placeOrder")
    suspend fun placeOrder(
        @Body orderSummary: OrderRequest, @Path("userId") userId: String
    ): CheckoutResponse
}
