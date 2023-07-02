package com.adelinarotaru.fooddelivery.customer.ui.cart

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.Cart
import com.adelinarotaru.fooddelivery.shared.networking.CartApi
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val dispatcher: CoroutineDispatcher, private val cartApi: CartApi) :
    BaseViewModel() {

    private val _cartProducts = MutableStateFlow<Cart?>(null)
    val cartProducts = _cartProducts.asStateFlow()

    fun fetchCartState() = viewModelScope.launch(dispatcher) {
        coRunCatching { cartApi.fetchCart() }.onSuccess { _cartProducts.value = it }
            .onFailure { sendError(it) }
    }

}