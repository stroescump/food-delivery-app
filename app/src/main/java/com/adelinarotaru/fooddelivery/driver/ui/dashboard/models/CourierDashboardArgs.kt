package com.adelinarotaru.fooddelivery.driver.ui.dashboard.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourierDashboardArgs(val name: String, val id: String) : Parcelable
