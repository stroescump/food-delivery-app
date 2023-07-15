package com.adelinarotaru.fooddelivery.driver.models

data class OrderStatusResponse(
    val status: Int,
    val courierId: String? = null,
    val courierName: String? = null
)
