package com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourierOrderAcceptedArgs(
    val orderId: String,
) : Parcelable
