package com.adelinarotaru.fooddelivery.shared.models

data class Order(
    val userId: Int,
    val restaurantId: Int,
    val status: Int,
    val orderDate: String,
    val total: Float,
    val review: UserReview? = null
)