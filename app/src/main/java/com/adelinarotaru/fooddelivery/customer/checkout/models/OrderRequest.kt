package com.adelinarotaru.fooddelivery.customer.checkout.models

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    val status: Int,
    @SerializedName("order_items")
    val orderItems: List<OrderItem>,
    @SerializedName("deliveryAddress")
    val address: String
)

data class OrderItem(
    @SerializedName("menu_item_id")
    val menuItemId: Int,
    @SerializedName("restaurant_id")
    val restaurantId: Int,
    val quantity: Int,
)