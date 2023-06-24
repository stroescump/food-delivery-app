package com.adelinarotaru.fooddelivery.driver.ui

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.driver.domain.CourierRepository
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.Order
import com.adelinarotaru.fooddelivery.shared.models.OrderStatus
import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import com.adelinarotaru.fooddelivery.shared.models.User
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourierDashboardViewModel(private val courierRepository: CourierRepository) :
    BaseViewModel() {

    private val _courierOrders = MutableStateFlow<List<CourierItemTask>>(emptyList())
    val courierOrders = _courierOrders.asStateFlow()

    fun fetchNearbyOrders() = viewModelScope.launch(Dispatchers.IO) {
        coRunCatching {
            delay(2000L)
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
            orderStatus = OrderStatus.DELIVERED,
            customerName = "Emily Wilson",
            customerPhone = "+1357924680",
            customerAddress = "654 Birch St, City, Country",
            restaurantName = "Pizza Paradise",
            restaurantAddress = "753 Walnut St, City, Country"
        ),

        CourierItemTask(
            orderId = "MNO345",
            orderStatus = OrderStatus.PREPARING,
            customerName = "Michael Davis",
            customerPhone = "+3698521470",
            customerAddress = "852 Elm St, City, Country",
            restaurantName = "Sushi Sensation",
            restaurantAddress = "159 Oak St, City, Country"
        )
    )

    private fun provideMocks() = listOf(
        Order(
            123412, 1, 3, 2, User(
                1,
                "Marius Stroescu",
                "stroescumarius@gmail.com",
                "notneeded",
                2,
                emptyList()
            ), Restaurant(
                3,
                "XO by Samir",
                "Str. Hallmark 221A, Entrance 2B",
                "+41745263546",
                4.8f,
                true,
                "11:20",
                "22:00",
                emptyList(),
                emptyList(),
                listOf("PIZZA", "BURGER", "KEBAB")
            ), null
        ),
        Order(
            id = 234523,
            userId = 2,
            restaurantId = 4,
            status = 1,
            user = User(
                id = 2,
                name = "John Smith",
                email = "johnsmith@example.com",
                password = "password123",
                userType = 1,
                orders = emptyList(),
                reviews = emptyList()
            ),
            restaurant = Restaurant(
                id = 4,
                name = "Taste of India",
                address = "Str. Spice 12",
                phoneNumber = "+41789012345",
                rating = 4.5f,
                freeDelivery = false,
                openingHours = "12:00",
                closingHours = "23:00",
                menuItems = emptyList(),
                reviews = emptyList(),
                cuisineTypes = listOf("INDIAN", "CURRY")
            ),
            review = null
        ),
        Order(
            id = 345634,
            userId = 3,
            restaurantId = 2,
            status = 3,
            user = User(
                id = 3,
                name = "Alice Johnson",
                email = "alice.johnson@example.com",
                password = "pass123",
                userType = 1,
                orders = emptyList(),
                reviews = emptyList()
            ),
            restaurant = Restaurant(
                id = 2,
                name = "Burger Joint",
                address = "Str. Burger 5",
                phoneNumber = "+41781234567",
                rating = 4.2f,
                freeDelivery = true,
                openingHours = "11:30",
                closingHours = "22:30",
                menuItems = emptyList(),
                reviews = emptyList(),
                cuisineTypes = listOf("BURGER", "FAST FOOD")
            ),
            review = null
        ),
        Order(
            id = 456745,
            userId = 4,
            restaurantId = 1,
            status = 2,
            user = User(
                id = 4,
                name = "Emma Wilson",
                email = "emma.wilson@example.com",
                password = "password",
                userType = 1,
                orders = emptyList(),
                reviews = emptyList()
            ),
            restaurant = Restaurant(
                id = 1,
                name = "La Pizzeria",
                address = "Str. Italia 10",
                phoneNumber = "+41761234567",
                rating = 4.7f,
                freeDelivery = true,
                openingHours = "10:00",
                closingHours = "23:30",
                menuItems = emptyList(),
                reviews = emptyList(),
                cuisineTypes = listOf("PIZZA", "ITALIAN")
            ),
            review = null
        ),
        Order(
            id = 567856,
            userId = 5,
            restaurantId = 6,
            status = 1,
            user = User(
                id = 5,
                name = "Sophia Lee",
                email = "sophia.lee@example.com",
                password = "pass1234",
                userType = 1,
                orders = emptyList(),
                reviews = emptyList()
            ),
            restaurant = Restaurant(
                id = 6,
                name = "Sushi Delight",
                address = "Str. Sakura 30",
                phoneNumber = "+41782345678",
                rating = 4.9f,
                freeDelivery = true,
                openingHours = "11:00",
                closingHours = "22:30",
                menuItems = emptyList(),
                reviews = emptyList(),
                cuisineTypes = listOf("SUSHI", "JAPANESE")
            ),
            review = null
        )
    )
}

