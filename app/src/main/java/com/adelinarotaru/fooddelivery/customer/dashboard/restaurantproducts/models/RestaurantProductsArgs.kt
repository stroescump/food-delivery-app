package com.adelinarotaru.fooddelivery.customer.dashboard.restaurantproducts.models

import android.os.Parcelable
import com.adelinarotaru.fooddelivery.shared.models.MenuItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantProductsArgs(val menuItems: List<MenuItem>, val restaurantName: String) : Parcelable
