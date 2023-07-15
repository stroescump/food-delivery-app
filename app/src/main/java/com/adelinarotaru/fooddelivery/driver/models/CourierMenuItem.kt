package com.adelinarotaru.fooddelivery.driver.models

import com.adelinarotaru.fooddelivery.driver.domain.IOrderItem
import com.adelinarotaru.fooddelivery.shared.base.ItemAdapter

data class CourierMenuItem(val menuItem: IOrderItem, val isPickedUp: Boolean, override val id: Int) :
    ItemAdapter