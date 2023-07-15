package com.adelinarotaru.fooddelivery.customer.checkout.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutArgs(
    val subtotal: Double,
    val delivery: Double,
    val total: Double = subtotal + delivery
) : Parcelable
