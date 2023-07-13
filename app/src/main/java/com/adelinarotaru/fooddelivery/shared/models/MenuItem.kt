package com.adelinarotaru.fooddelivery.shared.models

import com.adelinarotaru.fooddelivery.shared.ItemAdapter

data class MenuItem(
    override val id: Int,
    val name: String,
    val price: Double,
    val restaurantId: Int,
    val restaurantRating: Double,
    val foodCategory: String? = "Other",
    val ingredients: List<String> = emptyList(),
    val description: String? = null
) : ItemAdapter