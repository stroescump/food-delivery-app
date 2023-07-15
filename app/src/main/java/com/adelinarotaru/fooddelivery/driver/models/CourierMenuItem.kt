package com.adelinarotaru.fooddelivery.driver.models

import com.adelinarotaru.fooddelivery.driver.ui.dashboard.CourierItemTask
import com.adelinarotaru.fooddelivery.shared.base.ItemAdapter

data class CourierMenuItem(val restaurantInfo: CourierItemTask.RestaurantInfo, val isPickedUp: Boolean, override val id: Int) :
    ItemAdapter