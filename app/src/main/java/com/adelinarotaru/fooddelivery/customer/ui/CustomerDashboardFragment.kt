package com.adelinarotaru.fooddelivery.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.DependencyProvider
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.customer.data.RestaurantRepositoryImpl
import com.adelinarotaru.fooddelivery.databinding.FragmentCustomerDashboardBinding
import com.adelinarotaru.fooddelivery.shared.BaseFragment
import com.adelinarotaru.fooddelivery.utils.changeColorTo
import com.adelinarotaru.fooddelivery.utils.showMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerDashboardFragment :
    BaseFragment<FragmentCustomerDashboardBinding>(FragmentCustomerDashboardBinding::inflate) {
    override var binding: FragmentCustomerDashboardBinding? = null
    private lateinit var foodTypeAdapter: FoodTypeAdapter
    private lateinit var restaurantsAdapter: RestaurantsAdapter

    private val viewModel =
        CustomerDashboardViewModel(RestaurantRepositoryImpl(DependencyProvider.provideRestaurantApi()))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getRestaurants()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            searchView.changeColorTo(R.color.dirty_white)
            foodTypeAdapter = createFoodAdaptor().also { foodItemRv.adapter = it }
            restaurantsAdapter = createRestaurantsAdaptor().also { restaurantsRv.adapter = it }
        }
        updateAdapter(provideFoodItems())

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.restaurants.collect { restaurantList ->
                    if (restaurantList.isNullOrEmpty().not()) {
                        restaurantsAdapter.differ.submitList(restaurantList)
                    }
                }
            }
            launch {
                viewModel.error.collect {
                    showMessage(
                        requireContext(),
                        it?.message ?: getString(R.string.generic_error)
                    )
                }
            }
        }
    }

    private fun createRestaurantsAdaptor(): RestaurantsAdapter =
        RestaurantsAdapter { restaurantSelected ->
            // TODO implement onClick behavior
        }

    private fun createFoodAdaptor() = FoodTypeAdapter { itemSelected ->
        val updatedList = updateItemsState(itemSelected)
        updateAdapter(updatedList)
    }

    private fun updateItemsState(itemSelected: FoodTypeItem) =
        foodTypeAdapter.differ.currentList.toMutableList().map { foodTypeItem ->
            foodTypeItem.copy(
                isSelected = foodTypeItem.id == itemSelected.id && itemSelected.isSelected.not()
            )
        }.toMutableList()

    private fun updateAdapter(updatedList: MutableList<FoodTypeItem>) =
        foodTypeAdapter.differ.submitList(updatedList)

    companion object {
        private fun provideFoodItems() = buildList {
            add(FoodTypeItem(0, R.string.food_type_burger, R.drawable.ic_burger))
            add(FoodTypeItem(1, R.string.pizza, R.drawable.ic_pizza))
            add(FoodTypeItem(2, R.string.taco, R.drawable.ic_tacos))
            add(FoodTypeItem(3, R.string.sushi, R.drawable.ic_sushi))
            add(FoodTypeItem(4, R.string.ramen, R.drawable.ic_ramen))
            add(FoodTypeItem(5, R.string.kebab, R.drawable.ic_kebab))
            add(FoodTypeItem(6, R.string.donuts, R.drawable.ic_donut))
        }.toMutableList()
    }

}