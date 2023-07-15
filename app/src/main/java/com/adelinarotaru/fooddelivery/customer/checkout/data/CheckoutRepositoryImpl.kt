package com.adelinarotaru.fooddelivery.customer.checkout.data

import com.adelinarotaru.fooddelivery.customer.checkout.domain.CheckoutRepository
import com.adelinarotaru.fooddelivery.customer.checkout.domain.ICheckoutResponse
import com.adelinarotaru.fooddelivery.customer.checkout.models.OrderRequest
import com.adelinarotaru.fooddelivery.shared.networking.CheckoutApi

class CheckoutRepositoryImpl(private val checkoutApi: CheckoutApi) : CheckoutRepository {
    override suspend fun placeOrder(orderRequest: OrderRequest): ICheckoutResponse =
        checkoutApi.placeOrder(orderRequest)
}