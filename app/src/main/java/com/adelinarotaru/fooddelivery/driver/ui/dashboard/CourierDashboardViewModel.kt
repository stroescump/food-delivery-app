package com.adelinarotaru.fooddelivery.driver.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourierDashboardViewModel(private val courierRepository: CourierRepository) :
    BaseViewModel() {

    private val _courierOrders = MutableSharedFlow<List<CourierItemTask>>(replay = 1)
    val courierOrders = _courierOrders.asSharedFlow()

    fun fetchNearbyOrders(orderStatus: OrderStatus) = viewModelScope.launch(Dispatchers.IO) {
        coRunCatching {
            courierRepository.fetchNearbyOrders(orderStatus)
        }.onSuccess {
            _courierOrders.tryEmit(it)
        }.onFailure {
            sendError(it)
        }
    }

    fun fetchAcceptedOrders(courierId: String) = viewModelScope.launch(Dispatchers.IO) {
        coRunCatching {
            courierRepository.fetchAcceptedOrders(courierId)
        }.onSuccess {
            _courierOrders.tryEmit(it)
        }.onFailure {
            sendError(it)
        }
    }

    fun fetchAllOrders(courierId: String) = viewModelScope.launch(Dispatchers.IO) {
        coRunCatching {
            courierRepository.fetchAllOrders(courierId)
        }.onSuccess {
            _courierOrders.tryEmit(it)
        }.onFailure {
            sendError(it)
        }
    }

    fun getOrders() = _courierOrders.replayCache.first()

}

