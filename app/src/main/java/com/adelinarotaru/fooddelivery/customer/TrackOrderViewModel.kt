package com.adelinarotaru.fooddelivery.customer

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TrackOrderViewModel(
    private val dispatcher: CoroutineDispatcher, private val repository: CourierRepository
) : BaseViewModel() {

    private val _orderStatus = MutableStateFlow(OrderStatus.ORDER_RECEIVED)
    val orderStatus = _orderStatus.asStateFlow()

    private val _liveTracking = MutableStateFlow<LatLng?>(null)
    val liveTracking = _liveTracking.asStateFlow()

    private val _navigateToSuccess = MutableStateFlow(false)
    val navigateToSuccess = _navigateToSuccess.asStateFlow()

    fun startLiveTracking(): Job = viewModelScope.launch(dispatcher) {
        while (isActive) {
            delay(3000L)
            if (_orderStatus.value.orderStep == OrderStatus.PICKED_UP.orderStep) {
                coRunCatching {
                    Log.d(TrackOrderViewModel::class.java.simpleName, "polling")
                    repository.fetchCourierCoordinates()
                }.onSuccess { _liveTracking.value = it }.onFailure {
                    sendError(it)
                    breakFlow()
                }
            } else if (_orderStatus.value == OrderStatus.DELIVERED) {
                _navigateToSuccess.value = true
            }
        }
    }

    private fun breakFlow(): Nothing = throw CancellationException()

    fun trackOrder(orderId: String) = viewModelScope.launch {
        while (isActive) {
            delay(4000L)
            coRunCatching {
                repository.trackOrder(orderId)
            }.onSuccess { orderStatus ->
                _orderStatus.value = orderStatus
            }.onFailure {
                sendError(it)
                breakFlow()
            }
        }
    }
}