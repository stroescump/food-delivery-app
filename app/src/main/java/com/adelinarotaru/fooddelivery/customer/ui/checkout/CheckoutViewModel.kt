package com.adelinarotaru.fooddelivery.customer.ui.checkout

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.networking.CheckoutApi
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val checkoutApi: CheckoutApi
) : BaseViewModel() {

    private val _orderPlaced = MutableStateFlow<Boolean?>(null)
    val orderPlaced = _orderPlaced.asStateFlow()

    fun placeOrder() = viewModelScope.launch(dispatcher) {
        coRunCatching {
            checkoutApi.placeOrder()
        }.onSuccess {
            _orderPlaced.value = true
        }.onFailure { sendError(it) }
    }
}