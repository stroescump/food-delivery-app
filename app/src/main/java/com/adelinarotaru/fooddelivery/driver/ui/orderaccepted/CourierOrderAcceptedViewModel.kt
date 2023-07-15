package com.adelinarotaru.fooddelivery.driver.ui.orderaccepted

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.login.domain.ILocation
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

    fun markOrderDelivered(orderId: String) = viewModelScope.launch(dispatcher) {
        coRunCatching {
            courierRepository.markOrderDelivered(orderId)
        }.onSuccess { _navigateToSuccess.value = true }.onFailure { sendError(it) }
    }

    fun fetchClientCoordinates(address: String) = viewModelScope.launch(dispatcher) {
        coRunCatching {
            courierRepository.convertAddressToCoordinates(address = address)
        }.onSuccess { coordinates ->
            _clientCoordinates.update { coordinates }
        }.onFailure { sendError(it) }
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

}