package com.adelinarotaru.fooddelivery.admin.models

import com.adelinarotaru.fooddelivery.shared.base.ItemAdapter
import com.adelinarotaru.fooddelivery.shared.models.CuisineType

data class TrendingStatistics(
    val dishName: String,
    val dishCategory: CuisineType,
    val howManyTimesWasOrdered: Int,
    override val id: Int
) : ItemAdapter
