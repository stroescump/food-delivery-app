package com.adelinarotaru.fooddelivery.shared.models

import com.google.gson.annotations.SerializedName

data class Restaurant(
    val id: Int,
    val name: String,
    val lat: String,
    val long: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val rating: Float? = null,
    @SerializedName("free_delivery")
    val freeDelivery: Boolean = false,
    @SerializedName("opening_hours")
    val openingHours: String? = null,
    @SerializedName("closing_hours")
    val closingHours: String? = null,
    @SerializedName("menu_items")
    val menuItems: List<MenuItem>? = null,
    val reviews: List<UserReview>? = null,
    @SerializedName("cuisine_types")
    val cuisineTypes: List<String> = emptyList()
)