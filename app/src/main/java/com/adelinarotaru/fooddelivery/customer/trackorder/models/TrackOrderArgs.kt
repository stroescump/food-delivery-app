package com.adelinarotaru.fooddelivery.customer.trackorder.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackOrderArgs(
    val orderId: String,
    val deliveryAddress: String
) : Parcelable