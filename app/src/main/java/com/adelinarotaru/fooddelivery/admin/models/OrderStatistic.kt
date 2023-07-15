package com.adelinarotaru.fooddelivery.admin.models

import com.adelinarotaru.fooddelivery.shared.base.ItemAdapter

data class OrderStatistic(
    val firstName: String,
    val lastName: String,
    val orderDate: String,
    val amount: String,
    val isNewOrder: Boolean,
    override val id: Int
) : ItemAdapter
