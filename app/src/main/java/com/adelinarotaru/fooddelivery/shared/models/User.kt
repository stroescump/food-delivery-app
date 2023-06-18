package com.adelinarotaru.fooddelivery.shared.models

data class User(
    val userId: Int,
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val userTypeId: Int,
    val userType: Int, // Reference to the UserType
    val reviews: List<Review>
)
