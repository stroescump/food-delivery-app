package com.adelinarotaru.fooddelivery.driver.data.models

import com.adelinarotaru.fooddelivery.shared.login.domain.ILocation
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourierLocationResponse(
    override val latitude: String,
    override val longitude: String
) : ILocation