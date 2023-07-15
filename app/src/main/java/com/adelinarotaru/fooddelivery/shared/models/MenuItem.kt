package com.adelinarotaru.fooddelivery.shared.models

import android.os.Parcelable
import com.adelinarotaru.fooddelivery.shared.base.ItemAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem(
    override val id: Int,
    val name: String,
    val price: Double,
    val restaurantId: Int,
    val restaurantRating: Double,
    val foodCategory: String? = "Other",
    val ingredients: List<String> = emptyList(),
    val description: String? = null
) : ItemAdapter, Parcelable