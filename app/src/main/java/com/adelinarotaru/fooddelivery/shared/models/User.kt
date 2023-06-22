package com.adelinarotaru.fooddelivery.shared.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val userType: Int,
    val orders: List<Order>? = null,
    val reviews: List<UserReview>? = null
)

data class Restaurant(
    val id: Int,
    val name: String,
    val address: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val rating: Int?,
    @SerializedName("free_delivery")
    val freeDelivery: Boolean = false,
    @SerializedName("opening_hours")
    val openingHours: String?,
    @SerializedName("closing_hours")
    val closingHours: String?,
    @SerializedName("menu_items")
    val menuItems: List<MenuItem>? = null,
    val reviews: List<UserReview>? = null
)


data class MenuItem(
    val id: Int, val name: String, val price: Int, val restaurantId: Int, val restaurant: Restaurant
)

data class Order(
    val id: Int,
    val userId: Int,
    val restaurantId: Int,
    val status: Int,
    val user: User,
    val restaurant: Restaurant,
    val review: UserReview?
)

data class UserReview(
    val id: Int,
    val orderId: Int,
    val userId: Int,
    val restaurantId: Int,
    val rating: Int,
    val comment: String,
    val reviewDate: String,
    val order: Order,
    val user: User,
    val restaurant: Restaurant
)

