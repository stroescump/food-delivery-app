package com.adelinarotaru.fooddelivery.shared.models

import com.adelinarotaru.fooddelivery.admin.models.OrderStatistic

data class Order(
    val id: Int,
    val userId: Int,
    val restaurantId: Int,
    val status: Int,
    val user: User,
    val restaurant: Restaurant,
    val orderDate: String,
    val total: Float,
    val review: UserReview? = null
) {
    fun toOrderStatistic(): OrderStatistic = kotlin.run {
        val (firstName, lastName) = user.name.split(" ")
        OrderStatistic(
            firstName,
            lastName,
            orderDate,
            total.toString(),
            isNewOrder = true,
            orderDate.plus(firstName).plus(lastName).hashCode()
        )
    }
}