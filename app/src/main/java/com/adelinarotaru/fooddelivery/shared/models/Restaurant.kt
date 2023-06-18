package com.adelinarotaru.fooddelivery.shared.models

data class Restaurant(
    val restaurantId: Int,
    val name: String,
    val address: String,
    val phoneNumber: String,
    val email: String,
    val reviews: List<Review>
)