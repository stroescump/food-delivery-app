package com.adelinarotaru.fooddelivery.shared.models

import com.google.gson.annotations.SerializedName

data class Restaurant(
    val id: Int,
    val name: String,
    val lat: String,
    val long: String,
    val phoneNumber: String,
    val rating: Float? = null,
    val freeDelivery: Boolean = false,
    val openingHours: String? = null,
    val closingHours: String? = null,
    val menuItems: List<MenuItem>? = null,
    val reviews: List<UserReview>? = null,
    val cuisineTypes: List<String>
)