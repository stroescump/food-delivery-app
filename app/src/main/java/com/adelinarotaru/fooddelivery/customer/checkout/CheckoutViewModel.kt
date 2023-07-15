package com.adelinarotaru.fooddelivery.customer.checkout

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.customer.checkout.domain.CheckoutRepository
import com.adelinarotaru.fooddelivery.customer.checkout.models.OrderRequest
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val checkoutRepository: CheckoutRepository
) : BaseViewModel() {

    private val _orderPlaced = MutableStateFlow<String?>(null)
    val orderPlaced = _orderPlaced.asStateFlow()

    fun placeOrder(orderRequest: OrderRequest) = viewModelScope.launch(dispatcher) {
        coRunCatching {
            checkoutRepository.placeOrder(orderRequest)
        }.onSuccess {
            _orderPlaced.value = it.orderId
        }.onFailure { sendError(it) }
    }
}