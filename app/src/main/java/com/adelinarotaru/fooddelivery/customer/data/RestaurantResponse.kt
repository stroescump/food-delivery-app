package com.adelinarotaru.fooddelivery.customer.data

import com.adelinarotaru.fooddelivery.customer.domain.IMenuItem
import com.adelinarotaru.fooddelivery.customer.domain.IRestaurant
import com.adelinarotaru.fooddelivery.shared.models.UserReview
import com.google.gson.annotations.SerializedName

data class RestaurantResponse(
    override val id: Int,
    override val name: String,
    @SerializedName("latitude")
    override val lat: String,
    @SerializedName("longitude")
    override val long: String,
    @SerializedName("phone_number")
    override val phoneNumber: String,
    override val rating: Float,
    @SerializedName("free_delivery")
    override val freeDelivery: Boolean,
    @SerializedName("opening_hours")
    override val openingHours: String,
    @SerializedName("closing_hours")
    override val closingHours: String,
    @SerializedName("menu_items")
    override val menuItems: List<MenuItemResponse>?,
    override val reviews: List<UserReview>?,
    override val cuisineTypes: List<String>
) : IRestaurant

data class MenuItemResponse(
    override val id: Int,
    override val name: String,
    override val price: Double,
    override val restaurantId: Int,
    override val restaurantRating: Double,
    override val foodCategory: String,
    override val ingredients: String,
    override val description: String
) : IMenuItem
