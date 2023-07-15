package com.adelinarotaru.fooddelivery.customer.trackorder

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class TrackingUiModel(
    val orderStatus: OrderStatus = OrderStatus.ORDER_RECEIVED,
    val livePosition: LatLng? = null,
)

class TrackOrderViewModel(
    private val dispatcher: CoroutineDispatcher, private val repository: CourierRepository
) : BaseViewModel() {
    var courierName: String? = null

    private val _orderStatus = MutableStateFlow(TrackingUiModel())
    val orderStatus = _orderStatus.asStateFlow()

    private val _navigateToSuccess = MutableStateFlow(false)
    val navigateToSuccess = _navigateToSuccess.asStateFlow()

    private val _restaurantCheckpoints = MutableStateFlow<List<LatLng>?>(null)
    val restaurantCheckpoints = _restaurantCheckpoints.asStateFlow()

    fun startLiveTracking(orderId: String): Job = viewModelScope.launch(dispatcher) {
        while (isActive) {
            delay(3000L)
            if (_orderStatus.value.orderStatus.orderStep == OrderStatus.PICKED_UP.orderStep) {
                coRunCatching {
                    repository.fetchCourierCoordinates(orderId)
                }.onSuccess { coordinates ->
                    _orderStatus.update { it.copy(livePosition = coordinates) }
                }.onFailure {
                    sendError(it)
                    breakFlow()
                }
            } else if (_orderStatus.value.orderStatus == OrderStatus.DELIVERED) {
                _navigateToSuccess.value = true
            }
        }
    }

    private fun breakFlow(): Nothing = throw CancellationException()

    fun trackOrder(orderId: String) = viewModelScope.launch(dispatcher) {
        while (isActive) {
            delay(4000L)
            coRunCatching {
                repository.trackOrder(orderId)
            }.onSuccess { orderUpdates ->
                courierName = orderUpdates.courierName
                _orderStatus.update { it.copy(orderStatus = orderUpdates.status) }
            }.onFailure {
                sendError(it)
                breakFlow()
            }
        }
    }

    fun fetchRestaurantCheckpoints(orderId: String) = viewModelScope.launch(dispatcher) {
        coRunCatching {
            repository.fetchCourierCheckpoints(orderId)
        }.onSuccess { checkpoints ->
            _restaurantCheckpoints.update { checkpoints }
        }.onFailure { sendError(it) }
    }
}