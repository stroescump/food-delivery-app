package com.adelinarotaru.fooddelivery.shared.models

data class Order(
    val id: Int,
    val userId: Int,
    val restaurantId: Int,
    val status: Int,
    val user: User,
    val restaurant: Restaurant,
    val review: UserReview?
)