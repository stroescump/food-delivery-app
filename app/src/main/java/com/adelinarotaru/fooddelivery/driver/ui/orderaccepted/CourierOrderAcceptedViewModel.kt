package com.adelinarotaru.fooddelivery.driver.ui.orderaccepted

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.data.models.BatchCoordinatesRequest
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.data.models.CoordinatesRequest
import com.adelinarotaru.fooddelivery.driver.ui.orderaccepted.domain.AcceptOrderRequest
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.login.domain.ILocation
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CourierOrderAcceptedViewModel(
    private val dispatcher: CoroutineDispatcher, private val courierRepository: CourierRepository
) : BaseViewModel() {

    private val _navigateToSuccess = MutableStateFlow(false)
    val navigateToSuccess = _navigateToSuccess.asStateFlow()

    private val _clientCoordinates = MutableStateFlow<ILocation?>(null)
    val clientCoordinates = _clientCoordinates.asStateFlow()

    private val _optimizedRoute = MutableSharedFlow<List<ILocation>?>(1)
    val optimizedRoute = _optimizedRoute.asSharedFlow()

    private val _orderAccepted = MutableStateFlow<Boolean?>(null)
    val orderAccepted = _orderAccepted.asStateFlow()

    var batchRestaurantAddresses = emptyList<String>()

    fun updateOrderStatus(orderId: String, orderStatus: OrderStatus) =
        viewModelScope.launch(dispatcher) {
            coRunCatching {
                courierRepository.updateOrderStatus(orderId, orderStatus)
            }.onSuccess {
                if (orderStatus == OrderStatus.DELIVERED) _navigateToSuccess.value = true
            }.onFailure { sendError(it) }
        }

    fun fetchClientCoordinates(address: String) = viewModelScope.launch(dispatcher) {
        coRunCatching {
            setLoading(true)
            courierRepository.convertAddressToCoordinates(address = address)
        }.onSuccess { coordinates ->
            setLoading(false)
            _clientCoordinates.update { coordinates }
        }.onFailure {
            setLoading(false)
            sendError(it)
        }
    }

    fun optimizeRoute(orderId: String) = viewModelScope.launch(dispatcher) {
        setLoading(true)
        coRunCatching {
            courierRepository.fetchCourierCheckpoints(orderId)
        }.onSuccess { route ->
            setLoading(false)
            _optimizedRoute.tryEmit(route)
        }.onFailure {
            setLoading(false)
            sendError(it)
        }
    }

    fun acceptOrder(orderId: String, courierId: String) = viewModelScope.launch(dispatcher) {
        setLoading(true)
        coRunCatching {
            courierRepository.acceptOrder(
                orderId,
                AcceptOrderRequest(courierId.toInt(), OrderStatus.PREPARING.orderStep)
            )
        }.onSuccess {
            setLoading(false)
            _orderAccepted.value = true
        }.onFailure {
            setLoading(false)
            sendError(it)
        }
    }

    fun sendLocationUpdates(userId: String, latitude: Double, longitude: Double) =
        viewModelScope.launch(dispatcher) {
            coRunCatching {
                courierRepository.sendCourierLocationUpdates(
                    userId,
                    CoordinatesRequest(latitude.toString(), longitude.toString())
                )
            }.onSuccess { }.onFailure { sendError(it) }
        }

    fun getAddressFromRestaurantCoordinates(coordinatesRequest: List<CoordinatesRequest>) =
        viewModelScope.launch(dispatcher) {
            coRunCatching {
                courierRepository.getAddressFromCoordinates(
                    BatchCoordinatesRequest(
                        coordinatesRequest
                    )
                )
            }.onSuccess { res ->
                batchRestaurantAddresses = res.addresses
            }.onFailure { sendError(it) }
        }

}