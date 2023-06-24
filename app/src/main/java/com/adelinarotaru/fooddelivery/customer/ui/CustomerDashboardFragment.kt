package com.adelinarotaru.fooddelivery.customer.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.customer.data.RestaurantRepositoryImpl
import com.adelinarotaru.fooddelivery.databinding.FragmentCustomerDashboardBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.CuisineType.Asian
import com.adelinarotaru.fooddelivery.shared.models.CuisineType.Burger
import com.adelinarotaru.fooddelivery.shared.models.CuisineType.Donut
import com.adelinarotaru.fooddelivery.shared.models.CuisineType.Kebab
import com.adelinarotaru.fooddelivery.shared.models.CuisineType.Pizza
import com.adelinarotaru.fooddelivery.shared.models.CuisineType.Sushi
import com.adelinarotaru.fooddelivery.shared.models.CuisineType.Taco
import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import com.adelinarotaru.fooddelivery.utils.changeColorTo
import kotlinx.coroutines.launch

class CustomerDashboardFragment :
    BaseFragment<FragmentCustomerDashboardBinding, CustomerDashboardViewModel>(
        FragmentCustomerDashboardBinding::inflate
    ) {
    override var binding: FragmentCustomerDashboardBinding? = null
    private lateinit var foodTypeAdapter: FoodTypeAdapter
    private lateinit var restaurantsAdapter: RestaurantsAdapter

    override val viewModel =
        CustomerDashboardViewModel(RestaurantRepositoryImpl(DependencyProvider.provideRestaurantApi()))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            searchView.changeColorTo(R.color.dirty_white)
            foodTypeAdapter = createFoodAdaptor().also { foodItemRv.adapter = it }
            restaurantsAdapter = createRestaurantsAdaptor().also { restaurantsRv.adapter = it }
        }
        updateFoodItemsAdaptor(provideFoodItems())

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.restaurants.collect { restaurantList ->
                    if (restaurantList.isEmpty().not()) {
                        updateRestaurantsAdaptor(restaurantList)
                    }
                }
            }
        }

        viewModel.getRestaurants()
    }

    private fun createRestaurantsAdaptor(): RestaurantsAdapter =
        RestaurantsAdapter { restaurantSelected ->
            // TODO implement onClick behavior
        }

    private fun createFoodAdaptor() = FoodTypeAdapter { itemSelected ->
        val cuisineType = itemSelected.cuisineType.enum

        val filteredRestaurantsByCuisine = viewModel.filterRestaurantsByCuisine(cuisineType)

        updateRestaurantsAdaptor(filteredRestaurantsByCuisine)

        val updatedList = updateItemsState(itemSelected)
        if (updatedList.none { it.isSelected }) viewModel.getRestaurants()
        updateFoodItemsAdaptor(updatedList)
    }

    private fun updateRestaurantsAdaptor(newList: List<Restaurant>) =
        restaurantsAdapter.differ.submitList(newList)

    private fun updateItemsState(itemSelected: FoodTypeItem) =
        foodTypeAdapter.differ.currentList.toMutableList().map { foodTypeItem ->
            foodTypeItem.copy(
                isSelected = foodTypeItem.cuisineType.id == itemSelected.cuisineType.id && itemSelected.isSelected.not()
            )
        }.toMutableList()

    private fun updateFoodItemsAdaptor(updatedList: MutableList<FoodTypeItem>) =
        foodTypeAdapter.differ.submitList(updatedList)

    companion object {
        private fun provideFoodItems() = buildList {
            add(FoodTypeItem(cuisineType = Burger(0, R.string.burger, R.drawable.ic_burger)))
            add(FoodTypeItem(cuisineType = Pizza(1, R.string.pizza, R.drawable.ic_pizza)))
            add(FoodTypeItem(cuisineType = Taco(2, R.string.taco, R.drawable.ic_tacos)))
            add(FoodTypeItem(cuisineType = Sushi(3, R.string.sushi, R.drawable.ic_sushi)))
            add(FoodTypeItem(cuisineType = Asian(4, R.string.asian, R.drawable.ic_asian)))
            add(FoodTypeItem(cuisineType = Kebab(5, R.string.kebab, R.drawable.ic_kebab)))
            add(FoodTypeItem(cuisineType = Donut(6, R.string.donuts, R.drawable.ic_donut)))
        }.toMutableList()
    }

}