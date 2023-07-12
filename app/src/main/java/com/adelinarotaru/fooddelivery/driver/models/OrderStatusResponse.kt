package com.adelinarotaru.fooddelivery.driver.models

import com.adelinarotaru.fooddelivery.shared.models.OrderStatus

data class OrderStatusResponse(
    val status: OrderStatus,
    val courierId: String? = null,
    val courierName: String? = null
)
