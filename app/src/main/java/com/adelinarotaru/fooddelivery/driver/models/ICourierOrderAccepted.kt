package com.adelinarotaru.fooddelivery.driver.models

import com.adelinarotaru.fooddelivery.driver.domain.IOrderItem

interface ICourierOrderAccepted {
    val productsOrdered: List<IOrderItem>
}

fun ICourierOrderAccepted.toCourierMenuItem() = productsOrdered.mapIndexed { index, menuItem ->
    CourierMenuItem(menuItem = menuItem, isPickedUp = false, id = index)
}