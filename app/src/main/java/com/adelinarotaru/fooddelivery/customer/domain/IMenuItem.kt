package com.adelinarotaru.fooddelivery.customer.domain

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