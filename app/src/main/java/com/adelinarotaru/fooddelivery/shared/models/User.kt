package com.adelinarotaru.fooddelivery.shared.models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val userType: Int,
    val orders: List<Order>? = null,
    val reviews: List<UserReview>? = null
)

data class UserReview(
    val id: Int,
    val orderId: Int,
    val userId: Int,
    val restaurantId: Int,
    val rating: Int,
    val comment: String,
    val reviewDate: String,
)

