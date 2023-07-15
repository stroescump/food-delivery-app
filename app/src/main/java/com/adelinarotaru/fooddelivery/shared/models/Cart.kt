package com.adelinarotaru.fooddelivery.shared.models

import com.adelinarotaru.fooddelivery.customer.cart.models.CartMenuItem

data class Cart(val orderItems: List<CartMenuItem>)
