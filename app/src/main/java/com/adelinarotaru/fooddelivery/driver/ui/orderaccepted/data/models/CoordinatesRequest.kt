package com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.data.models

import com.adelinarotaru.fooddelivery.shared.login.domain.ILocation
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoordinatesRequest(
    override val latitude: String?,
    override val longitude: String?
) : ILocation
