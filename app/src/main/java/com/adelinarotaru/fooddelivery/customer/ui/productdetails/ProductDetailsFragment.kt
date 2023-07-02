package com.adelinarotaru.fooddelivery.customer.ui.productdetails

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.adelinarotaru.fooddelivery.databinding.FragmentProductDetailsBinding
import com.adelinarotaru.fooddelivery.shared.base.BaseFragment
import com.adelinarotaru.fooddelivery.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding, ProductDetailsViewModel>(
    FragmentProductDetailsBinding::inflate
) {

    private lateinit var productId: String

    companion object {
        fun newInstance(productId: String) = ProductDetailsFragment().apply {
            arguments = bundleOf(Constants.PRODUCT_ID_KEY to productId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = requireArguments().getString(Constants.PRODUCT_ID_KEY)
            ?: throw IllegalStateException("Cannot proceed without valid productId")
    }

    override val viewModel: ProductDetailsViewModel
        get() = TODO("Not yet implemented")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productDetails.collectLatest { productDetails ->
                if (productDetails == null) return@collectLatest

            }
        }

        viewModel.fetchProductDetails(productId)
    }

}