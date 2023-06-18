package com.adelinarotaru.fooddelivery.shared.models

data class Review(
    val reviewId: Int,
    val userId: Int,
    val restaurantId: Int,
    val rating: Double,
    val comment: String,
    val reviewDate: String
)