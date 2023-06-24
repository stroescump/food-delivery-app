package com.adelinarotaru.fooddelivery.shared.models

import androidx.annotation.DrawableRes
import com.adelinarotaru.fooddelivery.R

enum class OrderStatus(val formattedName: String, @DrawableRes val colorInt: Int) {
    REJECTED("Rejected", R.drawable.card_red_bg),
    ORDER_RECEIVED("Order Received", R.drawable.card_orange_bg),
    PREPARING("Preparing", R.drawable.card_yellow_bg),
    PICKED_UP("Picked Up", R.drawable.card_faded_dark_green_bg),
    DELIVERED("Delivered", R.drawable.card_crazy_green_bg)
}