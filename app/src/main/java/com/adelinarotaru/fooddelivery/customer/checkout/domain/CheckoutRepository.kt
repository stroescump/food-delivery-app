package com.adelinarotaru.fooddelivery.customer.checkout.domain

import com.adelinarotaru.fooddelivery.customer.checkout.models.OrderRequest

interface CheckoutRepository {
    suspend fun placeOrder(orderRequest: OrderRequest, userId: String): ICheckoutResponse
}