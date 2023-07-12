package com.adelinarotaru.fooddelivery.driver.models

import com.adelinarotaru.fooddelivery.shared.ItemAdapter
import com.adelinarotaru.fooddelivery.shared.models.MenuItem

data class CourierMenuItem(val menuItem: MenuItem, val isPickedUp: Boolean, override val id: Int) :
    ItemAdapter