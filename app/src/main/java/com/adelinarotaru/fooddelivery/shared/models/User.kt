package com.adelinarotaru.fooddelivery.shared.models

data class User(
    val userId: Int,
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val userTypeId: Int,
    val userType: UserType, // Reference to the UserType
    val reviews: List<Review>
)


data class UserType(
    val userTypeId: Int, val userType: USER_TYPE
)

enum class USER_TYPE {
    ADMIN, DRIVER, CUSTOMER
}

