package com.adelinarotaru.fooddelivery.driver.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClientDetails(
    val name: String, val address: String, val latitude: Double, val longitude: Double
) : Parcelable
