package com.adelinarotaru.fooddelivery.customer.dashboard

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.customer.dashboard.restaurantproducts.models.RestaurantProductsArgs
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
import com.adelinarotaru.fooddelivery.shared.models.MenuItem
import com.adelinarotaru.fooddelivery.shared.models.Restaurant
import com.adelinarotaru.fooddelivery.utils.changeColorTo
import kotlinx.coroutines.launch

class CustomerDashboardFragment :
    BaseFragment<FragmentCustomerDashboardBinding, CustomerDashboardViewModel>(
        FragmentCustomerDashboardBinding::inflate
    ) {
    private lateinit var foodTypeAdapter: FoodTypeAdapter
    private lateinit var restaurantsAdapter: RestaurantsAdapter

    private val navArgs by navArgs<CustomerDashboardFragmentArgs>()
    private val userName by lazy { navArgs.userName }
    private val userId by lazy { navArgs.userId }

    override val viewModel =
        CustomerDashboardViewModel(RestaurantRepositoryImpl(DependencyProvider.provideRestaurantApi()))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            greetingUser.text = getString(R.string.greeting_user, userName)
            searchView.apply {
                changeColorTo(R.color.dirty_white)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = true

                    override fun onQueryTextChange(newText: String?): Boolean {
                        val filteredList = filterDishesByQuery(newText)
                        restaurantsAdapter.differ.submitList(filteredList)
                        return true
                    }

                })
            }
            goToCart.setOnClickListener { navigateToCart() }
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

        viewModel.fetchRestaurants()
    }

    private fun filterDishesByQuery(queryText: String?) =
        queryText.takeIf { it?.isNotEmpty() == true }?.let {
            viewModel.restaurants.replayCache.last().filter { restaurant ->
                restaurant.menuItems?.any { dishes ->
                    dishes.name.contains(other = it, ignoreCase = true)
                } ?: false
            }
        } ?: viewModel.restaurants.replayCache.last()


    private fun createRestaurantsAdaptor(): RestaurantsAdapter =
        RestaurantsAdapter { restaurantSelected ->
            val menuItems = restaurantSelected.menuItems ?: return@RestaurantsAdapter
            goToRestaurantDetails(menuItems, restaurantSelected.name)
        }

    private fun goToRestaurantDetails(menuItems: List<MenuItem>, name: String) =
        findNavController().navigate(
            CustomerDashboardFragmentDirections.goToRestaurantProducts(
                restaurantProductArgs = RestaurantProductsArgs(
                    menuItems = menuItems, restaurantName = name
                )
            )
        )

    private fun navigateToCart() = findNavController().navigate(
        CustomerDashboardFragmentDirections.goToCart()
    )

    private fun createFoodAdaptor() = FoodTypeAdapter { itemSelected ->
        val cuisineType = itemSelected.cuisineType.enum

        val filteredRestaurantsByCuisine = viewModel.filterRestaurantsByCuisine(cuisineType)

        updateRestaurantsAdaptor(filteredRestaurantsByCuisine)

        val updatedList = updateItemsState(itemSelected)
        if (updatedList.none { it.isSelected }) viewModel.fetchRestaurants()
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