package com.adelinarotaru.fooddelivery.driver.models

import com.adelinarotaru.fooddelivery.shared.models.MenuItem

interface ICourierOrderAccepted {
    val productsOrdered: List<MenuItem>
}

fun ICourierOrderAccepted.toCourierMenuItem() = productsOrdered.mapIndexed { index, menuItem ->
    CourierMenuItem(menuItem = menuItem, isPickedUp = false, id = index)
}