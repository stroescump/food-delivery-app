package com.adelinarotaru.fooddelivery.customer.ui.cart

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.customer.models.CartMenuItem
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.Cart
import com.adelinarotaru.fooddelivery.shared.models.MenuItem
import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import com.adelinarotaru.fooddelivery.shared.networking.CartApi
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val dispatcher: CoroutineDispatcher, private val cartApi: CartApi) :
    BaseViewModel() {

    private val _cartProducts = MutableStateFlow<Cart?>(null)
    val cartProducts = _cartProducts.asStateFlow()

    fun fetchCartState() = viewModelScope.launch(dispatcher) {
        coRunCatching {
//            cartApi.fetchCart()

            val mockCartMenuItems: List<CartMenuItem> = listOf(
                CartMenuItem(
                    id = 1,
                    menuItem = MenuItem(
                        id = 1001,
                        name = "Cheeseburger",
                        price = 9.99,
                        restaurantId = 2001,
                        restaurant = Restaurant(
                            id = 2001,
                            name = "Burger Paradise",
                            lat = "654 Cedar St",
                            long = "",
                            rating = 4.5f,
                            phoneNumber = ""
                        ),
                        foodCategory = "Burgers"
                    ),
                    quantity = 2
                ),
                CartMenuItem(
                    id = 2,
                    menuItem = MenuItem(
                        id = 1002,
                        name = "Margherita Pizza",
                        price = 12.99,
                        restaurantId = 2002,
                        restaurant = Restaurant(
                            id = 2002,
                            name = "Pizza Haven",
                            lat = "654 Cedar St",
                            long = "",
                            rating = 4.2f,
                            phoneNumber = ""
                        ),
                        foodCategory = "Pizza"
                    ),
                    quantity = 1
                ),
                CartMenuItem(
                    id = 3,
                    menuItem = MenuItem(
                        id = 1003,
                        name = "Chicken Teriyaki",
                        price = 8.49,
                        restaurantId = 2003,
                        restaurant = Restaurant(
                            id = 2003,
                            name = "Asian Delight",
                            lat = "654 Cedar St",
                            long = "",
                            rating = 4.7f,
                            phoneNumber = ""
                        ),
                        foodCategory = "Asian Cuisine"
                    ),
                    quantity = 3
                ),
                CartMenuItem(
                    id = 4,
                    menuItem = MenuItem(
                        id = 1004,
                        name = "Spaghetti Bolognese",
                        price = 10.99,
                        restaurantId = 2004,
                        restaurant = Restaurant(
                            id = 2004,
                            name = "Italian Eats",
                            lat = "654 Cedar St",
                            long = "",
                            rating = 4.4f,
                            phoneNumber = ""
                        ),
                        foodCategory = "Italian Cuisine"
                    ),
                    quantity = 1
                ),
                CartMenuItem(
                    id = 5,
                    menuItem = MenuItem(
                        id = 1005,
                        name = "Sushi Combo",
                        price = 15.99,
                        restaurantId = 2005,
                        restaurant = Restaurant(
                            id = 2005,
                            name = "Sushi Express",
                            lat = "654 Cedar St",
                            long = "",
                            rating = 4.8f,
                            phoneNumber = ""
                        ),
                        foodCategory = "Japanese Cuisine"
                    ),
                    quantity = 2
                )
            )

            Cart(mockCartMenuItems)
        }.onSuccess { _cartProducts.value = it }
            .onFailure { sendError(it) }
    }

}