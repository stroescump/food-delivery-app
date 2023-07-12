package com.adelinarotaru.fooddelivery.driver.ui.orderaccepted

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.driver.models.CourierMenuItem
import com.adelinarotaru.fooddelivery.driver.models.toCourierMenuItem
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CourierOrderAcceptedViewModel(
    private val dispatcher: CoroutineDispatcher, private val courierRepository: CourierRepository
) : BaseViewModel() {

    private val _orderDetails = MutableStateFlow<List<CourierMenuItem>?>(null)
    val orderDetails = _orderDetails.asStateFlow()

    private val _navigateToSuccess = MutableStateFlow(false)
    val navigateToSuccess = _navigateToSuccess.asStateFlow()

    fun fetchOrderDetails(orderId: String) = viewModelScope.launch(dispatcher) {
        setLoading(true)
        coRunCatching {
            courierRepository.fetchOrderDetails(orderId)
        }.onSuccess { orderDetails ->
            _orderDetails.update { orderDetails.toCourierMenuItem() }
            setLoading(false)
        }.onFailure {
            sendError(it)
            setLoading(false)
        }
    }

    fun markOrderDelivered(orderId: String) = viewModelScope.launch(dispatcher) {
        coRunCatching {
            courierRepository.markOrderDelivered(orderId)
        }.onSuccess { _navigateToSuccess.value = true }.onFailure { sendError(it) }
    }
}