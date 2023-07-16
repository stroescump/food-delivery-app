package com.adelinarotaru.fooddelivery.customer.trackorder

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import com.google.android.gms.maps.model.LatLng
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

    private val jobList = mutableListOf<Job>()

    fun startLiveTracking(orderId: String): Job = viewModelScope.launch(dispatcher) {
        while (isActive) {
            delay(3000L)
            coRunCatching {
                repository.fetchCourierCoordinates(orderId)
            }.onSuccess { coordinates ->
                val latitude = coordinates.latitude?.toDouble() ?: return@onSuccess
                val longitude = coordinates.longitude?.toDouble() ?: return@onSuccess
                _orderStatus.update {
                    it.copy(
                        livePosition = LatLng(
                            latitude, longitude
                        )
                    )
                }
            }.onFailure {
                sendError(it)
            }
        }
    }.also { jobList.add(it) }

    fun trackOrder(orderId: String) = viewModelScope.launch(dispatcher) {
        while (isActive) {
            delay(4000L)
            coRunCatching {
                repository.trackOrder(orderId)
            }.onSuccess { orderUpdates ->
                courierName = orderUpdates.courierName

                _orderStatus.update { stateFlow ->
                    stateFlow.copy(
                        orderStatus = OrderStatus.values()
                            .first { it.orderStep == orderUpdates.status },
                    )
                }

                if (orderUpdates.status == OrderStatus.DELIVERED.orderStep) {
                    _navigateToSuccess.value = true
                    jobList.onEach { it.cancel() }
                }
            }.onFailure {
                sendError(it)
            }
        }
    }.also { jobList.add(it) }

    fun fetchRestaurantCheckpoints(orderId: String) = viewModelScope.launch(dispatcher) {
        coRunCatching {
            repository.fetchCustomerCheckpoints(orderId)
        }.onSuccess { checkpoints ->
            runCatching {
                checkpoints.takeIf { list -> list.all { it.latitude != null && it.longitude != null } }
                    ?: return@runCatching
                val coordinateList = checkpoints.map {
                    LatLng(
                        it.latitude!!.toDouble(), it.longitude!!.toDouble()
                    )
                }
                _restaurantCheckpoints.update { coordinateList }
            }.onFailure { sendError(Throwable("Please try reaching out this restaurant by phone. Location is unclear.")) }
        }.onFailure { sendError(it) }
    }.also { jobList.add(it) }

    fun clearJobs() = jobList.onEach { it.cancel() }
}