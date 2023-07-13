package com.adelinarotaru.fooddelivery.driver.domain

import com.adelinarotaru.fooddelivery.shared.models.Restaurant

interface IOrderItem {
    val name: String
    val restaurant: Restaurant
}