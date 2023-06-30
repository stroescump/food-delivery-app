package com.adelinarotaru.fooddelivery.admin.models

import com.adelinarotaru.fooddelivery.shared.ItemAdapter

data class RevenueStatistics(val totalRevenue: Float, override val id: Int) : ItemAdapter
