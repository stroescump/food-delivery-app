package com.adelinarotaru.fooddelivery.customer.dashboard.restaurantproducts

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adelinarotaru.fooddelivery.customer.data.RestaurantRepositoryImpl
import com.adelinarotaru.fooddelivery.databinding.FragmentRestaurantProductsBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.MenuItem

class RestaurantProductsFragment :
    BaseFragment<FragmentRestaurantProductsBinding, RestaurantProductsViewModel>(
        FragmentRestaurantProductsBinding::inflate
    ) {

    private val args by navArgs<RestaurantProductsFragmentArgs>()
    private val menuItems by lazy { args.restaurantProductArgs.menuItems }
    private val productsAdapter by lazy {
        RestaurantProductsAdapter { productClicked -> goToProductDetails(productClicked) }
    }

    private fun goToProductDetails(productClicked: MenuItem) = findNavController().navigate(
        RestaurantProductsFragmentDirections.goToProductDetails(
            productClicked
        )
    )

    override val viewModel: RestaurantProductsViewModel by lazy {
        RestaurantProductsViewModel(
            DependencyProvider.provideDispatcher(),
            RestaurantRepositoryImpl(DependencyProvider.provideRestaurantApi())
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            restaurantProducts.adapter = productsAdapter
            screenTitle.text = args.restaurantProductArgs.restaurantName
            goBack.setOnClickListener { findNavController().popBackStack() }
        }

        productsAdapter.differ.submitList(menuItems)
    }

}