package com.adelinarotaru.fooddelivery.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import com.adelinarotaru.fooddelivery.driver.ui.dashboard.CourierItemTask
import com.adelinarotaru.fooddelivery.shared.models.Cart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedViewModel : ViewModel() {
    private val _sessionState = MutableStateFlow(SessionState())
    val sessionState = _sessionState.asStateFlow()

    lateinit var courierItemTask: CourierItemTask

    fun updateCart(newCart: Cart) = _sessionState.update { it.copy(cartState = newCart) }

    fun updateUserId(newUserId: String): Boolean {
        _sessionState.update { it.copy(userId = newUserId) }
        return sessionState.value.userId != null
    }

    fun getCartItems() = _sessionState.value.cartState?.orderItems

    fun getCartState() = _sessionState.value.cartState
    fun destroyState() {
        Log.d("SessionState", "destroyState: called")
        _sessionState.update { SessionState() }
    }

    fun clearCartState() = _sessionState.update { it.copy(cartState = null) }

    fun getUserId(): String =
        _sessionState.value.userId
            ?: throw IllegalStateException("userId cannot be null at this point")
}

data class SessionState(val cartState: Cart? = null, val userId: String? = null)
