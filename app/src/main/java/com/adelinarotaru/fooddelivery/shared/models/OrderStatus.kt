package com.adelinarotaru.fooddelivery.shared.models

import androidx.annotation.DrawableRes
import com.adelinarotaru.fooddelivery.R

enum class OrderStatus(
    val formattedName: String, @DrawableRes val colorInt: Int, val orderStep: Int = -1
) {
    REJECTED("Rejected", R.drawable.card_red_bg),
    ORDER_RECEIVED("Order Received", R.drawable.card_orange_bg, orderStep = 0),
    PREPARING("Preparing", R.drawable.card_yellow_bg, orderStep = 1),
    PICKED_UP("Picked Up", R.drawable.card_faded_dark_green_bg, orderStep = 2),
    DELIVERED("Delivered", R.drawable.card_crazy_green_bg, orderStep = 3)
}