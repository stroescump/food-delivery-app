package com.adelinarotaru.fooddelivery.customer

import androidx.lifecycle.ViewModel
import com.adelinarotaru.fooddelivery.shared.models.Cart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedViewModel : ViewModel() {
    private val _sessionState = MutableStateFlow(SessionState(null))
    val sessionState = _sessionState.asStateFlow()

    fun updateCart(newCart: Cart) = _sessionState.update { it.copy(cartState = newCart) }

    fun getCurrentCart() = _sessionState.value.cartState?.orderItems
}

data class SessionState(val cartState: Cart? = null)
