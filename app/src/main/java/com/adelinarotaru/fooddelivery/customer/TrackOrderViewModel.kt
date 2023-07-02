package com.adelinarotaru.fooddelivery.customer

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrackOrderViewModel : BaseViewModel() {

    private val _orderStatus = MutableStateFlow(OrderStatus.ORDER_RECEIVED)
    val orderStatus = _orderStatus.asStateFlow()


    fun trackOrder(orderId: String) = viewModelScope.launch {
        coRunCatching { }.onSuccess { }.onFailure { sendError(it) }
    }
}