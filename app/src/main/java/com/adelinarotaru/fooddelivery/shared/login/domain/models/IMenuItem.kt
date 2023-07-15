package com.adelinarotaru.fooddelivery.shared.login.domain.models

interface IMenuItem {
    val id: Int
    val name: String
    val price: Double
    val restaurantId: Int
    val restaurantRating: Double
    val foodCategory: String
    val ingredients: String
    val description: String
}