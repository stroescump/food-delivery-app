package com.adelinarotaru.fooddelivery.customer.ui

import androidx.lifecycle.viewModelScope
import com.adelinarotaru.fooddelivery.customer.domain.RestaurantRepository
import com.adelinarotaru.fooddelivery.shared.base.BaseViewModel
import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import com.adelinarotaru.fooddelivery.utils.coRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CustomerDashboardViewModel(private val restaurantRepository: RestaurantRepository) :
    BaseViewModel() {

    private val _restaurants: MutableSharedFlow<List<Restaurant>> = MutableSharedFlow(replay = 1)
    val restaurants = _restaurants.asSharedFlow()

    init {
        _restaurants.tryEmit(emptyList())
    }

    fun getRestaurants() = viewModelScope.launch(Dispatchers.IO) {
        if (areRestaurantsCached()) {
            emitFromCache()
        } else coRunCatching { restaurantRepository.getRestaurants() }.onSuccess { refreshedRestaurants ->
            _restaurants.tryEmit(refreshedRestaurants)
        }.onFailure { err ->
            sendError(err)
        }
    }

    private fun emitFromCache() = _restaurants.tryEmit(_restaurants.replayCache.last())

    fun filterRestaurantsByCuisine(cuisineType: String): List<Restaurant> = try {
        restaurants.replayCache.last().toMutableList()
            .filter { it.cuisineTypes.contains(cuisineType) }
    } catch (e: NullPointerException) {
        emptyList()
    }

    private fun areRestaurantsCached(): Boolean = _restaurants.replayCache.last().isNotEmpty()
}
