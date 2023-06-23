package com.adelinarotaru.fooddelivery.shared.models

data class MenuItem(
    val id: Int, val name: String, val price: Int, val restaurantId: Int, val restaurant: Restaurant
)