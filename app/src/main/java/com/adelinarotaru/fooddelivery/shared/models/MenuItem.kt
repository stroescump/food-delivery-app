package com.adelinarotaru.fooddelivery.shared.models

data class MenuItem(
    val id: Int,
    val name: String,
    val price: Float,
    val restaurantId: Int,
    val restaurant: Restaurant,
    val foodCategory: String? = "Other",
    val ingredients: List<String> = emptyList(),
    val description: String? = null
)