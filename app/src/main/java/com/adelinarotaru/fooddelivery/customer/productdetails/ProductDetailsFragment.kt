package com.adelinarotaru.fooddelivery.customer.productdetails

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.adelinarotaru.fooddelivery.R
import com.adelinarotaru.fooddelivery.customer.cart.models.CartMenuItem
import com.adelinarotaru.fooddelivery.databinding.FragmentProductDetailsBinding
import com.adelinarotaru.fooddelivery.shared.DependencyProvider
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.shared.models.Cart
import com.adelinarotaru.fooddelivery.shared.models.MenuItem
import com.adelinarotaru.fooddelivery.utils.Constants
import com.adelinarotaru.fooddelivery.utils.showGenericError
import com.adelinarotaru.fooddelivery.utils.showJustMessage

class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding, ProductDetailsViewModel>(
    FragmentProductDetailsBinding::inflate
) {

    override val viewModel: ProductDetailsViewModel by lazy {
        ProductDetailsViewModel(
            DependencyProvider.provideDispatcher(), DependencyProvider.provideProductDetailsApi()
        )
    }

    private val navArgs by navArgs<ProductDetailsFragmentArgs>()
    private val menuItem by lazy { navArgs.menuItem }
    private val ingredientsAdapter by lazy { IngredientsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ingredients.adapter = ingredientsAdapter
            subtractQuantity.setOnClickListener { modifiyQuantity(SUBTRACT) }
            addQuantity.setOnClickListener { modifiyQuantity(ADD) }
            goBack.setOnClickListener { findNavController().popBackStack() }
            addToCart.setOnClickListener {
                val currentCart =
                    sharedViewModel.getCartState()?.orderItems?.toMutableList() ?: mutableListOf()
                val currentProduct = menuItem
                val currentQuantity = getCurrentQuantity()

                if (currentQuantity != null && currentQuantity > 0) {
                    val updatedProductList = currentCart.apply {
                        val indexToUpdate = indexOfFirst { it.menuItem.id == currentProduct.id }
                        if (indexToUpdate != NOT_FOUND) {
                            this[indexToUpdate] =
                                CartMenuItem(indexToUpdate, currentProduct, currentQuantity)
                        } else {
                            add(CartMenuItem(indexToUpdate.inc(), currentProduct, currentQuantity))
                        }
                    }
                    val updatedCartState =
                        sharedViewModel.getCartState()?.copy(orderItems = updatedProductList)
                            ?: Cart(updatedProductList)
                    sharedViewModel.updateCart(newCart = updatedCartState)

                    root.showJustMessage(
                        if (addToCart.text == getString(R.string.update)) getString(R.string.quantity_updated_successfully)
                        else getString(R.string.product_added_to_cart)
                    )
                    addToCart.text = getString(R.string.update)

                } else root.showJustMessage(getString(R.string.you_need_to_update_the_quantity))
            }

            updateUi(menuItem)
        }
    }

    private fun FragmentProductDetailsBinding.getCurrentQuantity() = try {
        Integer.parseInt(quantity.text.trim().toString())
    } catch (e: Throwable) {
        showGenericError()
        null
    }

    private fun FragmentProductDetailsBinding.modifiyQuantity(operation: String) {
        try {
            val currentQuantity = Integer.parseInt(quantity.text.trim().toString())
            quantity.text = if (operation == ADD) currentQuantity.inc().toString()
            else if (currentQuantity == 0) 0.toString() else currentQuantity.dec().toString()
        } catch (e: Throwable) {
            showGenericError()
        }
    }

    private fun FragmentProductDetailsBinding?.updateUi(productDetails: MenuItem) = this?.apply {
        detailsText.text = productDetails.description
        foodPrice.text = getString(R.string.priceFormatter, productDetails.price)
        foodName.text = productDetails.name
        foodCategory.text = productDetails.foodCategory
        rating.text = productDetails.restaurantRating.toString()
        ingredientsAdapter.differ.submitList(productDetails.ingredients?.mapIndexed { index, stringIngredient ->
            IngredientItem(
                stringIngredient.toIngredient(), index
            )
        })
    }

    companion object {
        private const val NOT_FOUND = -1
        private const val ADD = "ADD"
        private const val SUBTRACT = "SUBTRACT"
        fun newInstance(productId: String) = ProductDetailsFragment().apply {
            arguments = bundleOf(Constants.PRODUCT_ID_KEY to productId)
        }
    }
}