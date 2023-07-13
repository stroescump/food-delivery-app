package com.adelinarotaru.fooddelivery.driver.models

import com.adelinarotaru.fooddelivery.driver.domain.IOrderItem
import com.adelinarotaru.fooddelivery.shared.models.Restaurant

data class CourierOrderItem(override val name: String, override val restaurant: Restaurant) :
    IOrderItem
