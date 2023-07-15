package com.adelinarotaru.fooddelivery.driver.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.shared.models.User
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourierDashboardViewModel(private val courierRepository: CourierRepository) :
    BaseViewModel() {

    private val _courierOrders = MutableStateFlow<List<CourierItemTask>>(emptyList())
    val courierOrders = _courierOrders.asStateFlow()

    private val _courierProfile = MutableStateFlow<User?>(null)
    val courierProfile = _courierProfile.asStateFlow()

    init {
        fetchCourierProfile()
    }

    private fun fetchCourierProfile() = viewModelScope.launch(Dispatchers.IO) {
        coRunCatching {
            provideCourierProfileMock()
        }.onSuccess {
            _courierProfile.value = it
        }.onFailure {
            sendError(it)
        }
    }

    private fun provideCourierProfileMock() = User(
        id = 1,
        name = "John Doe",
        email = "johndoe@example.com",
        password = "password123",
        userType = 2
    )


    fun fetchNearbyOrders() = viewModelScope.launch(Dispatchers.IO) {
        coRunCatching {
//            delay(2000L)
//            provideMocks()
            provideCourierTaskMocks()
//            courierRepository.fetchNearbyOrders()
        }.onSuccess {
            _courierOrders.value = it
        }.onFailure {
            sendError(it)
        }
    }

    private fun provideCourierTaskMocks() = listOf(
        CourierItemTask(
            orderId = "ABC123",
            orderStatus = OrderStatus.ORDER_RECEIVED,
            customerName = "John Doe",
            customerPhone = "+1234567890",
            customerAddress = "123 Main St, City, Country",
            restaurantName = "Tasty Bites",
            restaurantAddress = "456 Elm St, City, Country"
        ),

        CourierItemTask(
            orderId = "DEF456",
            orderStatus = OrderStatus.PREPARING,
            customerName = "Jane Smith",
            customerPhone = "+9876543210",
            customerAddress = "789 Oak St, City, Country",
            restaurantName = "Spice World",
            restaurantAddress = "321 Pine St, City, Country"
        ),

        CourierItemTask(
            orderId = "GHI789",
            orderStatus = OrderStatus.PICKED_UP,
            customerName = "David Johnson",
            customerPhone = "+2468135790",
            customerAddress = "567 Maple St, City, Country",
            restaurantName = "Burger Kingdom",
            restaurantAddress = "987 Cedar St, City, Country"
        ),

        CourierItemTask(
            orderId = "JKL012",
            orderStatus = OrderStatus.REJECTED,
            customerName = "Emily Wilson",
            customerPhone = "+1357924680",
            customerAddress = "654 Birch St, City, Country",
            restaurantName = "Pizza Paradise",
            restaurantAddress = "753 Walnut St, City, Country"
        ),

        CourierItemTask(
            orderId = "MNO345",
            orderStatus = OrderStatus.REJECTED,
            customerName = "Michael Davis",
            customerPhone = "+3698521470",
            customerAddress = "852 Elm St, City, Country",
            restaurantName = "Sushi Sensation",
            restaurantAddress = "159 Oak St, City, Country"
        )
    )
}

