package com.adelinarotaru.fooddelivery.utils

import java.text.SimpleDateFormat
import java.util.Locale

object Constants {
    val dateFormatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    const val PRODUCT_ID_KEY = "PRODUCT_ID"
    const val ADMIN = 0
    const val CUSTOMER = 1
    const val COURIER = 2
    const val USER_TYPE = "USER_TYPE"

    const val REQUEST_DELAY = 5000L
}