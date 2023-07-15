package com.adelinarotaru.fooddelivery.customer.checkout.data

import com.adelinarotaru.fooddelivery.customer.checkout.domain.ICheckoutResponse

data class CheckoutResponse(override val orderId: String) : ICheckoutResponse
