package com.adelinarotaru.fooddelivery.customer.ui

import androidx.lifecycle.ViewModel
import com.adelinarotaru.fooddelivery.customer.domain.RestaurantRepository
import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CustomerDashboardViewModel(private val restaurantRepository: RestaurantRepository) :
    ViewModel() {

    private val _restaurants: MutableStateFlow<List<Restaurant>?> = MutableStateFlow(emptyList())
    val restaurants = _restaurants.asStateFlow()

    private val _error: MutableStateFlow<Throwable?> = MutableStateFlow(null)
    val error = _error.asStateFlow()

    suspend fun getRestaurants() =
        coRunCatching { restaurantRepository.getRestaurants() }.onSuccess { refreshedRestaurants ->
                _restaurants.update { refreshedRestaurants }
            }.onFailure { err ->
                _error.update { err }
            }
}
