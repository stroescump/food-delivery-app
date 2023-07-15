package com.adelinarotaru.fooddelivery.driver.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourierDashboardViewModel(private val courierRepository: CourierRepository) :
    BaseViewModel() {

    private val _courierOrders = MutableStateFlow<List<CourierItemTask>>(emptyList())
    val courierOrders = _courierOrders.asStateFlow()

    fun fetchNearbyOrders(orderStatus: OrderStatus) = viewModelScope.launch(Dispatchers.IO) {
        coRunCatching {
            courierRepository.fetchNearbyOrders(orderStatus)
        }.onSuccess {
            _courierOrders.value = it
        }.onFailure {
            sendError(it)
        }
    }

}

