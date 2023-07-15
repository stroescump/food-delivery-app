package com.adelinarotaru.fooddelivery.customer.checkout.models

data class OrderRequest(
    val userId: String,
    val status: Int,
    val orderItems: List<OrderItem>,
)

data class OrderItem(
    val menuItemId: Int,
    val restaurantId: Int,
    val quantity: Int,
)