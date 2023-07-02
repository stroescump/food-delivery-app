package com.adelinarotaru.fooddelivery.customer.models

import com.adelinarotaru.fooddelivery.shared.ItemAdapter
import com.adelinarotaru.fooddelivery.shared.models.MenuItem

data class CartMenuItem(
    override val id: Int,
    val menuItem: MenuItem,
    val quantity: Int
) : ItemAdapter
