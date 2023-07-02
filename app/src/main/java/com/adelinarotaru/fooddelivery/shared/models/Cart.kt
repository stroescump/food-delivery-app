package com.adelinarotaru.fooddelivery.shared.models

import com.adelinarotaru.fooddelivery.customer.models.CartMenuItem

data class Cart(val orderItems: List<CartMenuItem>)
