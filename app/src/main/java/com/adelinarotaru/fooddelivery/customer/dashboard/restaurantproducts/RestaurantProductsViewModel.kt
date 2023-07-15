package com.adelinarotaru.fooddelivery.customer.dashboard.restaurantproducts

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.customer.domain.RestaurantRepository
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class RestaurantProductsViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val restaurantRepository: RestaurantRepository
) : BaseViewModel() {

    fun getRestaurantMenuItems() = viewModelScope.launch(dispatcher) {
        coRunCatching { }.onSuccess {

        }
            .onFailure {
                sendError(it)
            }
    }
}