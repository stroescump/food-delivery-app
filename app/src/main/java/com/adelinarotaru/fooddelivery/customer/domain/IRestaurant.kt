package com.adelinarotaru.fooddelivery.customer.domain

import com.adelinarotaru.fooddelivery.shared.models.UserReview

interface IRestaurant {
    val id: Int
    val name: String
    val lat: String
    val long: String
    val phoneNumber: String
    val rating: Float
    val freeDelivery: Boolean
    val openingHours: String
    val closingHours: String
    val menuItems: List<IMenuItem>?
    val reviews: List<UserReview>?
    val cuisineTypes: List<String>
}