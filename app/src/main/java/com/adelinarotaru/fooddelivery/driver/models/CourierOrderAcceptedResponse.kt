package com.adelinarotaru.fooddelivery.driver.models

import com.adelinarotaru.fooddelivery.driver.models.ICourierOrderAccepted
import com.adelinarotaru.fooddelivery.shared.models.MenuItem

data class CourierOrderAcceptedResponse(
    override val productsOrdered: List<MenuItem>,
) : ICourierOrderAccepted
